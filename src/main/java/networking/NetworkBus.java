package networking;

import interfaces.INodeDataChangeListener;
import messaging.Message;
import messaging.MessageBroker;
import utils.Constants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkBus implements INodeDataChangeListener {
    ServerSocket serverSocket;
    private int port;
    private InetAddress address;
    private List<MessageBroker> messageBrokers;
    private ExecutorService executorService;

    public NetworkBus(String host, int port) throws UnknownHostException {
        this.port = port;
        this.address = InetAddress.getByName(host);
        executorService = Executors.newFixedThreadPool(Constants.QUEUE_CAPACITY);
        messageBrokers = new CopyOnWriteArrayList<MessageBroker>();

    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port, Constants.QUEUE_CAPACITY, address);

            while (true) {
                Node node = new Node(serverSocket.accept(), this);
                MessageBroker messageBroker = new MessageBroker(node);
                executorService.execute(node);
                executorService.execute(messageBroker);
                messageBrokers.add(messageBroker);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onNewPublisherData(Message data) {
        for (MessageBroker broker : messageBrokers) {
            if (!broker.getNode().isConnected()) {
                messageBrokers.remove(broker);
            } else {
                broker.publishData(data);
            }
        }
    }
}
