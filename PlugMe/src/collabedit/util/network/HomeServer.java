package collabedit.util.network;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import collabedit.users.CollabUserConfig;


public class HomeServer extends Thread{
	
	public static final int PORT = 8888;
	
	@SuppressWarnings("resource")
	public void run() {
		
		try{
			ServerSocket HomeSocket = new ServerSocket(PORT);
	        System.out.println("Server Started @ "+HomeSocket.getInetAddress().getHostAddress()+":"+
			                                       HomeSocket.getLocalPort());
			while(true)
	        {
	            Socket CONN = HomeSocket.accept();
	            System.out.println("Client @ "+CONN.getRemoteSocketAddress().toString().substring(1));
	            
	            if( CollabUserConfig.MyIP == null ) continue;
	            
	            if (CONN != null)
	            {
	                HomeServerHandler Connection = new HomeServerHandler(CONN);
	                Connection.start();
	            }
	        }
		}
		catch(IOException Error){}
    }

}
