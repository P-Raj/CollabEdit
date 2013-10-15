/**
 * 
 */
/**
 * @author Pranav Raj
 *
 */
package collabedit.changes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import collabedit.editors.Change;

public class Event implements Serializable{
	
	public static List<Event> IncomingEvents = new ArrayList<Event>();
	
	public static int CurrentVersion = -1;
	
	private static final long serialVersionUID = 7389739879L;

	enum Type { FileOpen , FileClose , FileChanged , None, VersionRequestSet, VersionRequestGet , HandShake};
	
	Type type;
	String FileName; 
	public Change FileChange;
	
	String GeneratedByIP;
	int VersionNumber = -1;
	String GeneratedByName;
	
	public Event(String username , String userid) {
		type = Type.None;
		FileName = "";
		FileChange = null;
		GeneratedByIP = userid;
		GeneratedByName = username;
	}
	
	public void IsHandShake(){
		this.type = Type.HandShake;
	}
	
	public void OpenedFile (String filename){
		type = Type.FileOpen;
		FileName = filename;
	}
	
	void ClosedFile (String filename) {
		type = Type.FileClose;
		FileName = filename;
	}
	
	public void ChangedFile ( Change filechange ){
		type = Type.FileChanged;
		FileName = filechange.Filename;
		FileChange = filechange;
	}

	public void VersionRequestGranted(int _VersionNum) {
		type = Type.VersionRequestSet;
		VersionNumber = _VersionNum;		
	}
	
	public void VersionRequester() {
		type = Type.VersionRequestGet;		
	}
	
}