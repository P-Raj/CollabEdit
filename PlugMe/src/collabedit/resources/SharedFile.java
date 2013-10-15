package collabedit.resources;

import java.util.ArrayList;
import java.util.List;

import collabedit.users.User;


public class SharedFile extends SharedObject{

	SharedFile( String filename , String fileid , List<User> actors )
	{
		Actors = actors;
		ObjectName = filename;
		ObjectID = fileid;
	}
	
	public static List<SharedFile> MyFiles  = new ArrayList<SharedFile>();
	
	public static boolean MyFilesContains ( String filename ){
		for (int FileIndex = 0; FileIndex < MyFiles.size(); FileIndex++ )
		{
			if (MyFiles.get(FileIndex).ObjectName.equals(filename))
				return true;
		}
		return false;
	}
	
	public void OpenInLocalSystem (String filepath , String filecontents)
	{
		
	}
	
	static void AddToMyFiles ( SharedFile File ){
		MyFiles.add(File);
	}
	
	static void RemoveFromMyFiles ( SharedFile File ){
		MyFiles.remove(File);
	}
	
	@Override
	public void ResolveClash() {
		// TODO Auto-generated method stub
		
	}


	
}
