package networking;

import interfaces.INodeDataChangeListener;
import messaging.Message;
import org.json.JSONArray;
import org.json.JSONObject;
import utils.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TransportBroker implements Runnable {

    private Node node;
    private INodeDataChangeListener nodeDataChangeListener;

    public TransportBroker(Node node, INodeDataChangeListener nodeDataChangeListener) {
        this.node = node;
        this.nodeDataChangeListener = nodeDataChangeListener;
    }

    private List<String> getSubjectTitles(JSONArray jsonArray){
        List<String> subjectTitles = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++){
            subjectTitles.add(jsonArray.getString(i));
        }
        return subjectTitles;
    }

    public void run() {

        List<String> subjectTitles = new ArrayList<String>();

        if (node.isConnected()) {
            System.out.println("Connected...");
            try {
                JSONObject message = node.receiveMessage();

                if (message.getString("type").equals("Subscriber")) {
                    node.setType(Constants.SUBSCRIBER);
                    subjectTitles = getSubjectTitles(message.getJSONArray("subjects"));
                    this.nodeDataChangeListener.onNewSubscriber(node, subjectTitles);
                } else if (message.getString("type").equals("Publisher")) {
                    node.setType(Constants.PUBLISHER);
                    subjectTitles = getSubjectTitles(message.getJSONArray("subjects"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        while (node.isConnected()) {
            if (node.getType() == Constants.PUBLISHER) {
                try {
                    if (node.isDataAvailable()){
                        JSONObject message = node.receiveMessage();
                        Message data = new Message(message.getJSONObject("data"), subjectTitles, message.getLong("_timeStamp"));
                        this.nodeDataChangeListener.onNewPublisherData(data);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
