package collabedit.util.network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;


public class MulticastClient {

	//Multicast to all systems/peers
	private static DatagramSocket SOCK = null;
    private static DatagramPacket PKT = null;
    private static byte[] MSG;
    
	private static final String MULTICAST_IP = "224.4.5.6";
	private static final int PORT = 6666;
	
	public static void doMultiCast(String payload)
	{
	    try {
	      SOCK = new DatagramSocket();
	      MSG = payload.getBytes();
	 
	      //Send to multicast IP address and port
	      InetAddress address = InetAddress.getByName(MULTICAST_IP);
	      PKT = new DatagramPacket(MSG, MSG.length, address, PORT);
	 
	      SOCK.send(PKT);
	      System.out.println("I send: " + payload);
	      SOCK.close();
	    } 
	    catch (IOException Error) {}
	}
}
