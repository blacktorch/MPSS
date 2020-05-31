package networking;

import interfaces.INodeDataChangeListener;
import messaging.Message;
import messaging.MessageBroker;
import utils.Constants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkBus implements INodeDataChangeListener {
    ServerSocket serverSocket;
    private String host;
    private int port;
    private InetAddress address;
    private MessageBroker messageBroker;
    private ExecutorService executorService;

    public NetworkBus(String host, int port) throws UnknownHostException {

        this.host = host;
        this.port = port;
        this.address = InetAddress.getByName(host);
        executorService = Executors.newFixedThreadPool(Constants.QUEUE_CAPACITY);

    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port, 50, address);
            //Node node;
            //TransportBroker transportBroker;

            while (true) {
                Node node = new Node(serverSocket.accept(), this);
                messageBroker = new MessageBroker(node);
                executorService.execute(node);
                executorService.execute(messageBroker);
                //Thread messageThread = new Thread(messageBroker);
               // messageThread.start();
                //transportBroker = new TransportBroker(node, this);
                //Thread transportThread = new Thread(transportBroker);
                //transportThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

//    public void onNewSubscriber(Node node, List<String> subjectTitles) {
//        messageBroker.addNodeToSubject(node, subjectTitles);
//    }

    public void onNewPublisherData(Message data) {
        messageBroker.publishData(data);
        //messageBroker.addSubjects(subjectTitles);
        //System.out.println(data.getData().getString("name") + " : " + subjectTitles.size());
    }
}
