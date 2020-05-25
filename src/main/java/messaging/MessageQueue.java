package messaging;

import java.util.Vector;

public class MessageQueue {
    /**
     * Number of strings (messages) that can be stored in the queue.
     */
    private int capacity;

    /**
     * The queue itself, all incoming messages are stored in here.
     */
    private Vector<String> queue = new Vector<String>(capacity);

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
     * @param message
     */
    public synchronized void send(String message) {
        //TODO check
    }

    /**
     * Receives a new message and removes it from the queue.
     *
     * @return
     */
    public synchronized String receive() {
        //TODO check
        return "0";
    }
}