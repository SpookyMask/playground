import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import server.ServerThread;

public class ServerApplication {

	public static void main(String[] args) {
		//connect to DB	*ToDo*
		
		//start listening

		LinkedList<ServerThread> users = new LinkedList<>();
		LinkedList<ServerThread> hosts = new LinkedList<>();
		
	    Socket s=null;
	    ServerSocket ss2=null;
	    try{
	        ss2 = new ServerSocket(4445); // can also use static final PORT_NUM , when defined
		    System.out.println("Server Listening......");
	    }
	    catch(IOException e){
	    	System.err.println("Server error");
	    }
	    
	    while(true){
	    	ServerThread st = null;
	        try{
	            s= ss2.accept();
	            st=new ServerThread(s, users, hosts);
	            st.start();
	            users.add(st);
	            System.out.println("Connection Established");
	        }

		    catch(Exception e){
	            users.remove(st);
		        System.out.println("Connection Error");
		    }
	    }
	}

}
