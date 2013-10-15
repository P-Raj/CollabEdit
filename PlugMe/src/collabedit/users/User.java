package collabedit.users;

import java.sql.Timestamp;

import collabedit.config.Config;

public class User {

	public String Username; 
	public String UserID; 
	public boolean IsAlive;
	public Timestamp Time;
	private java.util.Date Date;
	
	
	
	public static void SetCurrentUser( String OwnIP ){
		CollabUserConfig.MyIP = OwnIP;
		CollabUserConfig.IsUserSet = true;
	}
	
	public static void SetMasterIP( String IPAddr ){
		CollabUserConfig.MasterIP = IPAddr;
	}
	
	public User(){
		IsAlive = false;
	}
	
	public User( String username, String userid )
	{
		Username = username;
		UserID = userid;
		IsAlive = true;
		Time = GetCurrentTime();
	}
	
	public Timestamp GetCurrentTime()
	{
		Date = new java.util.Date();
		return (new Timestamp(Date.getTime()));
	}
	
	public void CheckAlive()
	{
		if ( (Date.getTime()-GetCurrentTime().getTime()) > Config.OffileTimePeriod ){
			IsAlive = false;
			System.out.println(Username + " is offline ");
		}
	}
	
	public void MakeAlive()
	{
		IsAlive = true;
		System.out.println(Username + " is alive ");
	}

	public void update() {
		// TODO Auto-generated method stub
		if (IsAlive == false)
			MakeAlive();
		Time = GetCurrentTime();
		
	}
	 
}
