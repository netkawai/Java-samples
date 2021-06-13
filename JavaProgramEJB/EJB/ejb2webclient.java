// EJB 2.0 client

public class HelloWebClient extends HttpServlet {

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException {
		response.setContextType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		String str = request.getParameter("t1");
		try {
			// open connection to Service Server.
			Context c = new InitialContext();
			// get interface information from XML of Statelessdemo.jar
			HelloRemoteHome home= (HelloRemoteHome)c.lookup("ejb/HelloBean");
			try {
				// create the concreat objct.
				HelloRemote remote = home.create();
				// use method
				String str = remote.display("Jack");
				System.out.println(str);
			}
			catch(RemoteException ex) {
				ex.printStatckTrace();
			}
			catch(javax.ejb.CreateException ex) {
				ex.printStackTrace();
			}
		}
		catch(NamingException ex) {
			ex.printStackTrance();
		}
		
		out.close();
	}
}


