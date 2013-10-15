package collabedit.editors;

import java.io.Serializable;

public class Change implements Serializable {

	public String Filename = null;
	public String Author = null;
	//public DocumentEvent ChangeEvent = null;
	public int ChangeOffset;
	public int ChangeLength;
	public String ChangeText;
	public String Type;
	
	public Change(String Filename , String Author , int ChangeOffset ,  int ChangeLength , String ChangeText , String Type)
	{
		//this.ChangeEvent = new DocumentEvent();
		this.Filename = Filename;
		this.Author = Author;
		this.ChangeLength = ChangeLength;
		this.ChangeOffset = ChangeOffset;
		this.ChangeText = ChangeText;
		this.Type = Type;
	}
	
}
