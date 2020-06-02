package com.chidiebere.networking;

import com.chidiebere.interfaces.INodeDataChangeListener;
import com.chidiebere.messaging.Message;
import com.chidiebere.messaging.MessageBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chidiebere.utils.Constants;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NetworkBus implements INodeDataChangeListener {
    private static Logger log = LoggerFactory.getLogger(NetworkBus.class);
    ServerSocket serverSocket;
    private int port;
    private InetAddress address;
    private List<MessageBroker> messageBrokers;
    private ExecutorService executorService;

    public NetworkBus(String host, int port) throws UnknownHostException {
        this.port = port;
        this.address = InetAddress.getByName(host);
        executorService = Executors.newFixedThreadPool(Constants.QUEUE_CAPACITY);
        messageBrokers = new CopyOnWriteArrayList<>();

    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port, Constants.QUEUE_CAPACITY, address);
            log.info("Service started at Host: " + address.getHostName() + " and Port: " + port);
            while (true) {
                Node node = new Node(serverSocket.accept(), this);
                MessageBroker messageBroker = new MessageBroker(node);
                executorService.execute(node);
                executorService.execute(messageBroker);
                messageBrokers.add(messageBroker);
                log.info("New node added with Id: " + node.getGUID());
                for (MessageBroker broker : messageBrokers) {
                    if (!broker.getNode().isConnected()) {
                        messageBrokers.remove(broker);
                        log.info(broker.getNode().getTypeName() + " node with Id: " + broker.getNode().getGUID() +
                                " has been disconnected and removed.");
                    }
                }

            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }

    }

    public void onNewPublisherData(Message data) {
        for (MessageBroker broker : messageBrokers) {
            if (!broker.getNode().isConnected()) {
                messageBrokers.remove(broker);
                log.info(broker.getNode().getTypeName() + " node with Id: " + broker.getNode().getGUID() +
                        " has been disconnected and removed.");
            } else {
                broker.publishData(data);
            }
        }
    }
}
