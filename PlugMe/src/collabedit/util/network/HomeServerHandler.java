package collabedit.util.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;

import collabedit.changes.Event;
import collabedit.changes.EventHandler;


public class HomeServerHandler extends Thread{
	
	private Socket ClientSocket;
 
    public HomeServerHandler(Socket SOCK) throws IOException{
        ClientSocket = SOCK;
    }
 
    public void run() 
    {
    	System.out.println("ServerHandler");
        try
        {    
            System.out.println("PROCESSING CLIENT : "+ClientSocket.getInetAddress().toString().substring(1));
            
            InputStream Channel = ClientSocket.getInputStream();
            ObjectInputStream Network = new ObjectInputStream(Channel);
            Event NewChange = (Event) Network.readObject();
            EventHandler.HandleEvent(NewChange);
            
            ClientSocket.close();
            Channel.close();
            Network.close();
            
        }
        
        catch(Exception Error)
        {
            System.out.println("Error: " + Error);
        }
    }

}
