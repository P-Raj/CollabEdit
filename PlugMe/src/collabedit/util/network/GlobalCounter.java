package collabedit.util.network;

public class GlobalCounter {
	
	public static int REV_COUNTER = 0;
	
	public static int getCurRevision(){	
		REV_COUNTER += 1;
		return REV_COUNTER;
	}

}
