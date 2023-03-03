
import java.util.List;
import java.util.ArrayList;
import org.epics.ca.*;


import java.util.Properties;

public class CAClient {

    private Context context = null;

    public void connect(String channelName){
    	Channel<Double> channel = null;
        try {
            System.out.println(" ------> Connecting to "+channelName);
            channel = context.createChannel( channelName, Double.class );
            channel.connectAsync().get(30, java.util.concurrent.TimeUnit.SECONDS);
            if (channel.getConnectionState() == ConnectionState.CONNECTED) {
            	System.out.println(" ................. Connected to "+channelName);
            } else {
            	System.out.println("................. FAILED");
            }
        } catch (Throwable th) {
        	if (channel != null) {
        		if (channel.getConnectionState() == ConnectionState.CONNECTED) {
                	System.out.println(" ................. Connected to "+channelName);
                } else {
                	System.out.println("................. FAILED");
                }
        	}
            th.printStackTrace();
        }
    }

    public void run(String[] channelNames){
        final Properties properties = new Properties();
        
        try {
        	context = new Context(properties);
        	for (String channel: channelNames) {
                connect(channel);
            } 
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
        	System.exit(0);
        }
    }

    /***
     * Compile and run
     * javac -cp <path/to/>ca-1.3.2-all.jar TestJavaClient.java
     * java -cp .:<path/to/>ca-1.3.2-all.jar TestJavaClient <pv1> <pv2>
     */
    public static void main(String[] args) {

        // check command-line arguments
        if (args.length < 1) {
            System.out.println("usage: java " + CAClient.class.getName() + " <pvname1> <pvname2> ...");
            System.exit(1);
        }

        // execute
        new CAClient().run(args);
    }
}