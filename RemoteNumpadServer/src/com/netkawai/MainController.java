package com.netkawai;

import java.awt.AWTException;;

public class MainController {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TcpServer tcpServer = new TcpServer();
		
		 VirtualNumpad numpadListener = null;
		try {
		    numpadListener = new VirtualNumpad();
		}
		catch (AWTException e) {
		    System.err.println("Unable to generate system events.");
		    return;
		}
		
		tcpServer.addNumpadListener(numpadListener);
		tcpServer.open();
		
		try {
			tcpServer.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}			
	}

}
