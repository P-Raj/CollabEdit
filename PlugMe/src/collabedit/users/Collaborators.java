package collabedit.users;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import collabedit.changes.Event;
import collabedit.util.network.HomeClient;
 

public class Collaborators {

	public static List<User> Peers = new ArrayList<User>();
	private static Hashtable <String,User> CollabUsers = new Hashtable<String,User>();
	
	
	
	public static void AddUser( String HostName , String IPAddr )
	{
		System.out.println("Trying to add user :" + IPAddr + " " + HostName);
		if (CollabUsers.containsKey(IPAddr)){
			User Host = CollabUsers.get(IPAddr);
			CollabUsers.remove(IPAddr);
			Host.update();
			CollabUsers.put(IPAddr, Host);
			System.out.println("User already exists");
		}
		else
		{
			User Host = new User(HostName,IPAddr);
			CollabUsers.put(IPAddr,Host);
			Peers.add(Host);
			System.out.println("HOST : " + HostName);
			Event handshake = null;
			try {
				handshake = new Event( InetAddress.getLocalHost().getHostName() , CollabUserConfig.MyIP);
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handshake.IsHandShake();
			HomeClient.Unicast(IPAddr, handshake);
			System.out.println("User Created");
		}
		
		
	}
	
	public static List<String> GetOnlineUsers()
	{
		System.out.println("Getting online users :");
		List<String> OnlineUsers = new ArrayList<String>();
		for(User usr : Peers){
			if (usr.IsAlive){
				OnlineUsers.add(usr.UserID);
				System.out.print(" " + usr.UserID + " ");
			}
		}
		return OnlineUsers;
	}
	
	
	/***
	public List<User> getOnlineCollaborators() {
		
		User Peer;
		List<User> OnlinePeers = new ArrayList<User>();
		for(int PeerIndex = 0 ; PeerIndex < Peers.size() ; PeerIndex++ )
		{
			Peer = Peers.get(PeerIndex);
			if (Peer.IsAlive) OnlinePeers.add(Peer);
		}
		
		return OnlinePeers;
		
	}
	
	public List<User> getOnlineCollaoratorsNetwork(){
		// check network for new online collabs
		return null;
	}

	public void PropogateChange( Event change ){
		// send changes to all the users
	}
	***/
}
