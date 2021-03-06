/*
 * Copyright (c) 2020, Chidiebere
 * */

package com.chidiebere;

import com.chidiebere.networking.NetworkBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.UnknownHostException;

/**
 * @author Chidiebere Onyedinma
 * **/
public class MPSService {

    private static Logger log = LoggerFactory.getLogger(MPSService.class);
    private static String IP = "localhost";
    private static int PORT = 12345;
    public static void main(String[] args) throws UnknownHostException {
        if (args.length > 1 && !args[0].equals("")){
            IP = args[0];
            PORT = Integer.parseInt(args[1]);
        }
        log.info("Service Launched");
        NetworkBus networkBus = new NetworkBus(IP, PORT);
        networkBus.start();
    }

}
