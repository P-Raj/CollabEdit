package collabedit.resources;

import java.util.List;

import collabedit.users.User;


public abstract class SharedObject {

	enum ObjectPermission {RW , R};
	
	public String ObjectName; 
	public String ObjectID;
	public List<User> Actors;
	private boolean IsCurrent;
	public String Owner;
	public ObjectPermission Permission;
	
	
	
	SharedObject()
	{
		IsCurrent = false;
		Permission = ObjectPermission.RW;
	}
	
	public abstract void ResolveClash();
	
	
}
