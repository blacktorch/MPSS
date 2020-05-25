package networking;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.UnknownHostException;

public class NetworkBus {
    ServerSocket serverSocket;
    private String host;
    private int port;
    private InetAddress address;

    public NetworkBus(String host, int port) throws UnknownHostException {

        this.host = host;
        this.port = port;
        this.address = InetAddress.getByName(host);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(port, 50, address);
            Node node;
            TransportBroker transportBroker;
            while (true) {
                node = new Node(serverSocket.accept());
                transportBroker = new TransportBroker(node);
                Thread transportThread = new Thread(transportBroker);
                transportThread.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
