package networking;

import interfaces.INodeDataChangeListener;
import messaging.Message;
import messaging.MessageBroker;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.List;

public class NetworkBus implements INodeDataChangeListener {
    ServerSocket serverSocket;
    private String host;
    private int port;
    private InetAddress address;
    private MessageBroker messageBroker;

    public NetworkBus(String host, int port) throws UnknownHostException {

        this.host = host;
        this.port = port;
        this.address = InetAddress.getByName(host);
        messageBroker = new MessageBroker();
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port, 50, address);
            Node node;
            TransportBroker transportBroker;
            Thread messageThread = new Thread(messageBroker);
            messageThread.start();
            while (true) {
                node = new Node(serverSocket.accept());
                transportBroker = new TransportBroker(node, this);
                Thread transportThread = new Thread(transportBroker);
                transportThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onNewSubscriber(Node node, List<String> subjectTitles) {
        messageBroker.addNodeToSubject(node, subjectTitles);
    }

    public void onNewPublisherData(Message data) {
        messageBroker.publishData(data);
        //messageBroker.addSubjects(subjectTitles);
        //System.out.println(data.getData().getString("name") + " : " + subjectTitles.size());
    }
}
