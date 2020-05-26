import networking.NetworkBus;

import java.net.UnknownHostException;

public class MPSSCore {

    public static void main(String[] args) throws UnknownHostException {
        NetworkBus networkBus = new NetworkBus("192.168.1.85", 12345);
        networkBus.start();
    }

}
