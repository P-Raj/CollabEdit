package collabedit.util.network;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class SetupNetworking {
	
	public static void Setup(){
		
		//Initializing Home Server 
		(new HomeServer()).start();
		
		// Initializing Multicast Listener
		(new MulticastListener()).start();
		
		//Recurring Multicast every 1 minutes with "LIVE" Message
		ScheduledExecutorService BroadcastSerivice = Executors.newScheduledThreadPool(1);
		
		BroadcastSerivice.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                
            	System.out.println("Sending I AM ALIVE Packet");
            	
            	String msg = null;
				try {
					msg = "LIVE:"+InetAddress.getLocalHost().getHostName();
				} 
				catch (UnknownHostException Error) {}
				
            	MulticastClient.doMultiCast(msg);
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
         }, 0, 1, TimeUnit.MINUTES);
		
	}
		
}
