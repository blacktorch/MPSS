import networking.NetworkBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

public class MPSService {

    private static Logger log = LoggerFactory.getLogger(MPSService.class);
    private static String IP = "192.168.1.85";
    private static int PORT = 12345;
    public static void main(String[] args) throws UnknownHostException {
        log.info("Service Launched");
        NetworkBus networkBus = new NetworkBus(IP, PORT);
        networkBus.start();
    }

}
