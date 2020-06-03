/*
 * Copyright (c) 2020, Chidiebere
 * */

package com.chidiebere.messaging;

import com.chidiebere.interfaces.INewMessageListener;
import com.chidiebere.networking.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.chidiebere.utils.Constants;

/**
 * @author Chidiebere Onyedinma
 * **/
public class MessageBroker implements Runnable {
    private static Logger log = LoggerFactory.getLogger(MessageBroker.class);
    private MessageQueue messageQueue;
    private INewMessageListener listener;
    private Node node;

    public MessageBroker(Node listener){
        this.messageQueue = new MessageQueue(Constants.QUEUE_CAPACITY);
        this.node = listener;
        this.listener = this.node;
    }

    public synchronized void publishData(Message data){
        messageQueue.send(data);
    }


    public Node getNode() {
        return node;
    }

    public void run() {
        while (node.isConnected()){
            if (!messageQueue.isEmpty()){
                Message message = messageQueue.receive();
                listener.onNewPublishedMessage(message);
            }
        }
    }
}
