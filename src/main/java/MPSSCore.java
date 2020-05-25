import networking.NetworkBus;

import java.net.UnknownHostException;

public class MPSSCore {

    public static void main(String[] args) throws UnknownHostException {
        NetworkBus networkBus = new NetworkBus("localhost", 12345);
        networkBus.start();
    }

}
