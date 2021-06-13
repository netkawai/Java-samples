package com.netkawai;


import java.io.*;
import java.net.*;
import java.util.*;

public class TcpServer extends Thread {
	 public static final int DEFAULT_PORT = 4576;
	    private int port;
	    private ServerSocket serverSocket;
	    private Socket clientSocket;
	    private Collection<INumpadListener> numpadListeners;
//	    private Collection<IStatusListener> statusListeners;

	    public TcpServer() {
	        this.port = 4576;
	        this.numpadListeners = new HashSet<INumpadListener>();
//	        this.statusListeners = new HashSet<IStatusListener>();
	    }

	    public TcpServer(int port) {
	        this.port = port;
	        this.numpadListeners = new HashSet<INumpadListener>();
//	        this.statusListeners = new HashSet<IStatusListener>();
	    }
	    
	    private void polling()
        {
	    	do
	    	{
            try {
                this.clientSocket = this.serverSocket.accept();
                Exception error = null;
                try {
                    OutputStreamWriter out = new OutputStreamWriter(this.clientSocket.getOutputStream());
                    try {
                        InputStream inputStream = this.clientSocket.getInputStream();
                        try {
                            InputStreamReader reader = new InputStreamReader(inputStream);
                            try {
                                String keyName;
                                char eventType;
                                try {
                                    BufferedReader in = new BufferedReader(reader);
                                    try {
                                        this.changeStatus("TCP.2");

                                        do {
                                        		String input;
	                                            try {
	                                                input = in.readLine();
	                                            }
	                                            catch (SocketException e) {
	                                                break;
	                                            }
	                                            if (input == null) {
	                                                break;
	                                            }
	                                            System.out.println(input);
	                                            if (input.toLowerCase().equals("name")) {
	                                                String name = InetAddress.getLocalHost().getHostName();
	                                                out.write(String.valueOf(name) + '\n');
	                                                out.flush();
	                                                continue;
	                                            }
	                                            eventType = input.charAt(0);
	                                            keyName = input.substring(1);
	                                            if (eventType == '+') {
	                                                for(INumpadListener listener : this.numpadListeners) {
	                                                    listener.onKeyPressed(keyName);
	                                                }
	                                            }
	                                            if (eventType != '-') continue;
	                                            
	                                            for(INumpadListener listener : this.numpadListeners) {
	                                                listener.onKeyReleased(keyName);
	                                            }

                                        } while (true);
                                    }
                                    finally {
                                        if (in != null) {
                                            in.close();
                                        }
                                    }
                                }
                                catch (Exception e) {
                                }
                            }
                            finally {
                                if (reader != null) {
                                    reader.close();
                                }
                            }
                        }
                        catch (Exception e) {
                        }
                    }
                    finally {
                        if (out != null) {
                            out.close();
                        }
                    }
                }
                catch (Throwable var2_7) {
                }
                this.clientSocket.close();
                this.clientSocket = null;
                this.changeStatus("TCP.3");
                continue;
            }
            catch (SocketException e) {
                continue;
            }
            catch (Exception e) {
                System.err.println("Something went wrong: " + e.getMessage());
            }
        	}
            while (!this.serverSocket.isClosed());
        }
	    
	    /*
	     * Unable to fully structure code
	     * Enabled aggressive exception aggregation
	     */
	    @Override
	    public void run() {
	        try {
	            this.serverSocket = new ServerSocket(this.port);
	            this.changeStatus("TCP.0");

	        }
	        catch (IOException e) {
	            System.err.println("Unable to start the server: " + e.getMessage());
	            return;
	        }
	        polling();
	    }


	    public void addNumpadListener(INumpadListener listener) {
	        this.numpadListeners.add(listener);
	    }


	    public void removeNumpadListener(INumpadListener listener) {
	        this.numpadListeners.remove(listener);
	    }


//	    public void addStatusListener(IStatusListener listener) {
//	        this.statusListeners.add(listener);
//	    }

//	    public void removeStatusListener(IStatusListener listener) {
//	        this.statusListeners.remove(listener);
//	    }


	    public void open() {
	        this.start();
	    }

	    public void close() {
	        try {
	            if (this.clientSocket != null) {
	                this.clientSocket.close();
	            }
	            this.serverSocket.close();
	            this.join();
	            this.changeStatus("TCP.1");
	        }
	        catch (Exception e) {
	            System.err.println("Error while closing the server: " + e.getMessage());
	        }
	    }

	    private void changeStatus(String status) {
	        //for (IStatusListener listener : this.statusListeners) {
	        //    listener.onStatusChanged(status);
	        //}
	    }

}
