package collabedit.changes;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.widgets.Display;

import collabedit.editors.Change;
import collabedit.editors.MainEditor;
import collabedit.users.CollabUserConfig;
import collabedit.users.Collaborators;
import collabedit.util.network.GlobalCounter;
import collabedit.util.network.HomeClient;

public class EventHandler {

	// defines what actions to take when Event X happens
	private static Event event = null;
	public static List<Event> MyChanges = new ArrayList<Event>();
	public static List<Event> OthersChanges = new ArrayList<Event>();
	
	public EventHandler () {
		MyChanges = new ArrayList<Event>();
		OthersChanges = new ArrayList<Event>();
	}
	
	public static void SendEvent ( Event event , List<String> Receivers ){	
		//Collaborators.GetOnlineUsers()
		HomeClient.SendChangeObject(Receivers, event);
	}
	
	public static Event GenerateEventFileOpen(String Filename){
		event = new Event("",CollabUserConfig.MyIP);
		event.OpenedFile(Filename);
		return event;
		
	}
	
	private static void VersionRequestHandler( Event VersionReq ) 
	{
	   //Send Request to Server for a new Version Number
		if (CollabUserConfig.AmIHost)
		{
			int CurrentVersion = GlobalCounter.getCurRevision();
			List<String> RequestUser = new ArrayList<String>();
			RequestUser.add(VersionReq.GeneratedByIP);
			Event ReqGranted = new Event("",CollabUserConfig.MyIP);
			ReqGranted.VersionRequestGranted(CurrentVersion);
			SendEvent(ReqGranted, RequestUser);
		}
	}
		
	public static void UpdateDoc(final Event event)
	{
		MainEditor.MainDoc.removeDocumentListener(MainEditor.MainDocUpdater);
		
		Display.getDefault().syncExec( new Runnable() {
		
		public void run() {
			synchronized(this)
			{
			//Showing that change in editor
			int a = 0 , b = 0;
			String c;
			if (event.FileChange.Type.equals("Add"))
			{
				a = event.FileChange.ChangeOffset;
				c =  event.FileChange.ChangeText;
				System.out.println("Adding " + c + " at position " + a);
				try {
					MainEditor.MainDoc.replace(a , 0 , c);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (event.FileChange.Type.equals("Delete"))
			{
				a = event.FileChange.ChangeOffset;
				b = event.FileChange.ChangeLength;
				System.out.println("Deleting from " + a + " to " + b);
				try {
					MainEditor.MainDoc.replace(a,b,"");
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else if (event.FileChange.Type.equals("Replace"))
			{
				a = event.FileChange.ChangeOffset;
				b = event.FileChange.ChangeLength;
				c =  event.FileChange.ChangeText;
				System.out.println("Replaced with " + c);
				try {
					MainEditor.MainDoc.replace(a,b,c);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		  }
		}
		});
		
		MainEditor.MainDoc.addDocumentListener(MainEditor.MainDocUpdater);
	}
	
	public static Event GenerateEventFileChanged(Change Changes)
	{
		// Get the version number
		
		// make it a stack
		Event e = null;
		try {
			e = new Event(InetAddress.getLocalHost().getHostName(), CollabUserConfig.MyIP);
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		e.ChangedFile(Changes);
		MyChanges.add(e);
		return e;
		
	}
	
	public static void VersionRequestSender()
	{
		Event VersionRequest = new Event("",CollabUserConfig.MyIP);
		VersionRequest.VersionRequester();
		List<String> Master = new ArrayList<String>();
		Master.add(CollabUserConfig.MasterIP);
		SendEvent(VersionRequest,Master);
	}
		
	public static void HandleEvent( Event event ) throws BadLocationException{
				
			if (event.type == Event.Type.HandShake){
				System.out.println("Handshake");
				System.out.print(event.GeneratedByName + " " + event.GeneratedByIP);
				System.out.println();
				Collaborators.AddUser(event.GeneratedByName, event.GeneratedByIP);
			}
			
			else if (event.type == Event.Type.VersionRequestGet){
				System.out.println("Master got request for version number");
				VersionRequestHandler(event);
				System.out.println("Generated event number :" + GlobalCounter.REV_COUNTER + " Sent ");
			}
			
			else if (event.type == Event.Type.VersionRequestSet){
				System.out.println("Got response from the master ");
				System.out.println("Setting Version number : " + event.VersionNumber);				
			}
			else if (event.type == Event.Type.FileOpen){
				// show notification to the user that another user has opened the file
				System.out.println("User" + event.GeneratedByIP +" opened the file " + event.FileName);
			}
			else if (event.type == Event.Type.FileClose){
				System.out.println("User" + event.GeneratedByIP +" closed the file " + event.FileName);
			}
			else if (event.type == Event.Type.FileChanged){
				if (MainEditor.editor != null){
					System.out.println("User" + event.GeneratedByIP +" changed the file ");
					OthersChanges.add(event);
					System.out.println(event.GeneratedByName + " generated following change :");
					System.out.println(event.FileChange.Type);
					UpdateDoc(event);
				}
				//System.out.println("User" + event.GeneratedBy.Username +" changed the file ");
			}
			else{
				System.out.println("User" + event.GeneratedByIP +" unknown event");
			}
		
	}
	
	
}
