package networking;

import org.json.JSONObject;
import utils.Constants;

import java.io.IOException;

public class TransportBroker implements Runnable {

    private Node node;

    public TransportBroker(Node node) {
        this.node = node;
    }

    public void run() {

        if (node.isConnected()) {
            System.out.println("Connected...");
            try {
                JSONObject message = node.receiveMessage();

                if (message.getString("type").equals("Subscriber")) {
                    node.setType(Constants.SUBSCRIBER);
                } else if (message.getString("type").equals("Publisher")) {
                    node.setType(Constants.PUBLISHER);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (node.isConnected()) {
            if (node.getType() == Constants.SUBSCRIBER) {
                try {
                    JSONObject message = node.receiveMessage();
                    System.out.println(message.getString("data"));
                    node.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
