import gov.aps.jca.JCALibrary;
import gov.aps.jca.Channel;
import gov.aps.jca.Context;
import gov.aps.jca.dbr.DBRType;
import gov.aps.jca.dbr.DBR;
import gov.aps.jca.dbr.STRING;
import java.util.List;
import java.util.ArrayList;


public class JCAClient {

    private Context context = null;

    public Channel connect(String channelName){
        try {
            System.out.println(" ------> Connecting to "+channelName);
            Channel channel = context.createChannel(channelName);
            context.pendIO(30.0);
            if (channel.getConnectionState() != Channel.ConnectionState.CONNECTED) {
                System.out.println("................. FAILED");
            } else {
                System.out.println(" ................. Connected to "+channelName);
            }
            return channel;
        } catch (Throwable th) {
            th.printStackTrace();
            return null;
        }
    }

    public void run(String[] channelNames){

        try {
            JCALibrary jca = JCALibrary.getInstance();
            context = jca.createContext(JCALibrary.CHANNEL_ACCESS_JAVA);

            List<Channel> connectedChannels = new ArrayList<>();
            for (String channel: channelNames) {
                connectedChannels.add(connect(channel));
            }

            // Close
            for (int i = 0; i < connectedChannels.size(); i++){
                if (connectedChannels.get(i) != null) {
                    connectedChannels.get(i).destroy();
                }
            }

            context.flushIO();
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            try {
                // Destroy the context, check if never initialized.
                if (context != null)
                    context.destroy();
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    /***
     * Compile and run
     * javac -cp <path/to/>jca-2.4.8-SNAPSHOT.jar TestJavaClient.java
     * java -cp .:<path/to/>jca-2.4.8-SNAPSHOT.jar TestJavaClient <pv1> <pv2>
     */
    public static void main(String[] args) {

        // check command-line arguments
        if (args.length < 1) {
            System.out.println("usage: java " + JCAClient.class.getName() + " <pvname1> <pvname2> ...");
            System.exit(1);
        }

        // execute
        new JCAClient().run(args);
    }
}