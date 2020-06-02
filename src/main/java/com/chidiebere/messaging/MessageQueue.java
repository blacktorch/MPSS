package com.chidiebere.messaging;

import java.util.Vector;

public class MessageQueue {
    /**
     * Number of strings (messages) that can be stored in the queue.
     */
    private int capacity;

    /**
     * The queue itself, all incoming messages are stored in here.
     */
    private Vector<Message> queue = new Vector<>(capacity);

    /**
     * Constructor, initializes the queue.
     *
     * @param capacity The number of messages allowed in the queue.
     */
    public MessageQueue(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Adds a new message to the queue. If the queue is full, it waits until a message is released.
     *
     * @param message a message to added to the queue
     */
    public synchronized void send(Message message) {
        if (queue.size() < capacity ){
            this.queue.add(message);
        }
    }

    /**
     * Receives a new message and removes it from the queue.
     *
     * @return returns a message to be published
     */
    public synchronized Message receive() {
        Message message = queue.firstElement();
        queue.remove(message);
        return message;
    }

    public boolean isEmpty(){
        return queue.isEmpty();
    }
}