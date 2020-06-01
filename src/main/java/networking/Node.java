package networking;

import interfaces.INewMessageListener;
import interfaces.INodeDataChangeListener;
import messaging.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.Constants;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Node  implements INewMessageListener, Runnable {
    private static Logger log = LoggerFactory.getLogger(Node.class);
    private Socket nodeSocket;
    private OutputStream out;
    private InputStream in;
    private InetAddress address;
    private boolean connected;
    private int type;
    private String typeName;
    private volatile boolean terminated = false;
    private INodeDataChangeListener nodeDataChangeListener;
    private List<String> subjectTitles;
    private long GUID;

    public Node(Socket socket, INodeDataChangeListener nodeDataChangeListener){
        this.nodeSocket = socket;
        this.nodeDataChangeListener = nodeDataChangeListener;
        GUID = new Random(System.currentTimeMillis()).nextLong();
        subjectTitles = new ArrayList<String>();
        typeName = "UNDEFINED";

        if (nodeSocket.isConnected()){
            log.info("Node with Id: " + GUID + " has been connected.");
            try {
                out = nodeSocket.getOutputStream();
                in = nodeSocket.getInputStream();
                this.connected = true;
                this.address = nodeSocket.getInetAddress();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }

        }

    }

    public synchronized JSONObject receiveMessage() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        while ((read = in.read()) != 0) {
            if (read == -1){
                terminate();
                throw new IOException();
            }
            buffer.write(read);
        }
        String message = buffer.toString();
        try {
            return new JSONObject(message);
        } catch(JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public synchronized void sendMessage(JSONObject json) throws IOException {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(out);
            osw.write(json.toString());
            osw.write(0);
            osw.flush();
        }
        catch (SocketException e){
            terminate();
            System.out.println("Node has been disconnected");
        }

    }

    public boolean isDataAvailable() throws IOException {
        return in.available() > Constants.MINIMUM_BUFFER_SIZE;
    }

    private void removeNode() {
        if (nodeSocket != null){
            try {
                log.info("Terminating " + typeName + " node with Id: " + GUID + " and closing socket.");
                nodeSocket.close();
            }
            catch(IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
        connected = false;
    }

    public synchronized int getType(){
        return type;
    }

    public synchronized boolean isConnected(){
        return connected && !terminated;
    }

    public synchronized InetAddress getAddress(){
        return address;
    }

    public synchronized void setType(int type){
        this.type = type;
    }

    public synchronized void terminate(){
        terminated = true;
        removeNode();
    }

    public synchronized long getGUID(){
        return GUID;
    }

    public synchronized String getTypeName(){
        return typeName;
    }

    private synchronized List<String> getSubjectTitles(JSONArray jsonArray){
        List<String> subjectTitles = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); i++){
            subjectTitles.add(jsonArray.getString(i));
        }
        return subjectTitles;
    }

    public void setNodeDataChangeListener(INodeDataChangeListener nodeDataChangeListener) {
        this.nodeDataChangeListener = nodeDataChangeListener;
    }

    private void checkAndRemoveNode( int timeout){
        try {
            if (!nodeSocket.getInetAddress().isReachable(timeout)){
                terminate();
                log.info(typeName + " node with Id: " + GUID + " has been disconnected");
            }
        } catch (IOException e){
            /*terminate node if the pipe is broken*/
            terminate();
            log.error(typeName + " node with Id: " + GUID + " has been terminated because it could not be reached.");
        }
    }

    public void run() {


        if (!terminated && connected) {

            try {
                JSONObject message = receiveMessage();
                if (message.getString(Constants.TYPE).equals(Constants.SUB)) {
                    type = Constants.SUBSCRIBER;
                    typeName = Constants.SUB;
                    subjectTitles = getSubjectTitles(message.getJSONArray(Constants.SUBJECTS));
                    log.info("Node with Id: " + GUID + " has been identified as a Subscriber to subjects: " +
                            subjectTitles.toString());
                } else if (message.getString(Constants.TYPE).equals(Constants.PUB)) {
                    type = Constants.PUBLISHER;
                    typeName = Constants.PUB;
                    subjectTitles = getSubjectTitles(message.getJSONArray(Constants.SUBJECTS));
                    log.info("Node with Id: " + GUID + " has been identified as a Publisher to the subjects: " +
                            subjectTitles.toString());
                } else {
                    terminate();
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        while (!terminated && connected){

            if (type == Constants.PUBLISHER) {
                try {
                    if (isDataAvailable()){
                        JSONObject message = receiveMessage();
                        assert message != null;
                        Message data = new Message(message.getJSONObject(Constants.DATA), subjectTitles,
                                message.getLong(Constants.TIME_STAMP));
                        this.nodeDataChangeListener.onNewPublisherData(data);
                    } else {
                        /*Check if the publisher node is still alive*/
                        checkAndRemoveNode(Constants.PUBLISHER_HEARTBEAT_TIMEOUT);

                    }

                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }else if(type == Constants.SUBSCRIBER) {
                /*Check if the subscriber node is still alive*/
                checkAndRemoveNode(Constants.SUBSCRIBER_HEARTBEAT_TIMEOUT);
            }

        }
    }

    public synchronized void onNewPublishedMessage(Message message) {
        try {
            if (type == Constants.SUBSCRIBER){
                for (String subject : message.getSubjectTitles()){
                    if (subjectTitles.contains(subject)){
                        message.getData().put(Constants.SUBJECT, subject);
                        log.info("Message with subject - " + subject + " is being published to Subscriber" +
                                " node with Id: " + GUID);
                        sendMessage(message.getData());
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
