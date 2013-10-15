package collabedit.util.network;

import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.List;

import collabedit.changes.Event;

public class HomeClient {
	
	public static final int PORT = 8888;
	
	public static void SendChangeObject( List<String> IP_LIST , Event CHANGE ){
		
		for( String IP : IP_LIST ){
			Unicast( IP, CHANGE );
		}
	}
	
	// Do Multiple Unicast to send changes to all peers
	public static void Unicast( String PEER_IP, Event CHANGE_OBJ ){
		
		try
        {
			Socket SOCK = new Socket(PEER_IP, PORT);
			
			OutputStream Channel = SOCK.getOutputStream();
			ObjectOutputStream Network = new ObjectOutputStream(Channel);
			Network.writeObject(CHANGE_OBJ);
			
			SOCK.close();
			Channel.close();
			Network.close();
        }
        catch (Exception Error) {}
	}
	
}
