package collabedit.util.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.NetworkInterface;

import collabedit.users.CollabUserConfig;
import collabedit.users.Collaborators;
import collabedit.users.User;


public class MulticastListener extends Thread{

	private static MulticastSocket SOCK = null;
    private static DatagramPacket PKT = null;
    private static byte[] MSG;
    
    private static final String MULTICAST_IP = "224.4.5.6";
	private static final int PORT = 6666;
	
	public void run(){
		
		MSG = new byte[1024];
	    
		System.out.println("Thread running");
	    try {
	      SOCK = new MulticastSocket(PORT);
	      InetAddress ADDR = InetAddress.getByName(MULTICAST_IP);
	      SOCK.joinGroup(ADDR);
	 
	      while (true) {
	        PKT = new DatagramPacket(MSG, MSG.length);
	        SOCK.receive(PKT);
	        
	        String DATA = new String(MSG, 0, PKT.getLength());
	        
	        String Peer = PKT.getAddress().toString().substring(1);
	        InetAddress SENDER_IP = InetAddress.getByName(Peer);
	        
	        if( NetworkInterface.getByInetAddress(SENDER_IP) == null )
	        {
	        	String MSGType = DATA.split(":")[0];
	        	String PeerHostName = null;
	        	try{
	        		PeerHostName = DATA.split(":")[1];
	        	}
	        	catch(Exception Error){}
	        	
	        	
	        	if( MSGType.equalsIgnoreCase("LIVE") )
	        	{
	        		System.out.println("From " + Peer + " Msg : " + DATA);
		        	System.out.println("Peer Name: " + PeerHostName  );
		        	
		        	Collaborators.AddUser(PeerHostName, Peer);
		        	
		        	if( CollabUserConfig.AmIHost ) MulticastClient.doMultiCast("HOST");
	        	}
	        	
	        	if( MSGType.equalsIgnoreCase("HOST") ){
	        		if( !CollabUserConfig.AmIHost ) User.SetMasterIP(Peer);
	        	}
	        }
	        
	        else if( !CollabUserConfig.IsUserSet ){ User.SetCurrentUser(SENDER_IP.toString().substring(1));}
	        
	      }
	    } 
	    catch (IOException Error) {}
	}
}
