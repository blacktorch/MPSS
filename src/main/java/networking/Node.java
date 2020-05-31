package networking;

import interfaces.INewMessageListener;
import interfaces.INodeDataChangeListener;
import messaging.Message;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import utils.Constants;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;


public class Node  implements INewMessageListener, Runnable {
    private Socket nodeSocket;
    private OutputStream out;
    private InputStream in;
    private InetAddress address;
    private boolean connected;
    private int type;
    private volatile boolean terminated = false;
    private INodeDataChangeListener nodeDataChangeListener;
    private List<String> subjectTitles;

    public Node(Socket socket, INodeDataChangeListener nodeDataChangeListener){
        this.nodeSocket = socket;
        this.nodeDataChangeListener = nodeDataChangeListener;
        subjectTitles = new ArrayList<String>();

        if (nodeSocket.isConnected()){
            System.out.println("Connected...");
            try {
                out = nodeSocket.getOutputStream();
                in = nodeSocket.getInputStream();
                this.connected = true;
                this.address = nodeSocket.getInetAddress();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public synchronized JSONObject receiveMessage() throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int read;
        while ((read = in.read()) != 0) {
            if (read == -1){
                removeNode();
                throw new IOException();
            }
            buffer.write(read);
        }
        String message = buffer.toString();
        try {
            JSONObject json = new JSONObject(message);
            return json;
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
                nodeSocket.close();
            }
            catch(IOException ignored) {}
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ignored) {}
        connected = false;
    }

    public int getType(){
        return type;
    }

    public boolean isConnected(){
        return connected && !terminated;
    }

    public synchronized InetAddress getAddress(){
        return address;
    }

    public void setType(int type){
        this.type = type;
    }

    public synchronized void terminate(){
        terminated = true;
        removeNode();
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

    public void run() {


        if (!terminated && connected) {

            try {
                JSONObject message = receiveMessage();
                System.out.println(message.toString());
                if (message.getString("type").equals("Subscriber")) {
                    this.setType(Constants.SUBSCRIBER);
                    System.out.println("Subscriber registered");
                    subjectTitles = getSubjectTitles(message.getJSONArray("subjects"));
                } else if (message.getString("type").equals("Publisher")) {
                    this.setType(Constants.PUBLISHER);
                    subjectTitles = getSubjectTitles(message.getJSONArray("subjects"));
                } else {
                    terminate();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        while (!terminated && connected){

            if (type == Constants.PUBLISHER) {
                System.out.println("--------I am a pub---------");
                try {
                    if (isDataAvailable()){
                        JSONObject message = receiveMessage();
                        assert message != null;
                        Message data = new Message(message.getJSONObject(Constants.DATA), subjectTitles, message.getLong(Constants.TIME_STAMP));
                        this.nodeDataChangeListener.onNewPublisherData(data);
                        //System.out.println(data.getData().toString());
                    } else {
                        /*Check if the publisher is still alive*/
                        try {
                            if (!nodeSocket.getInetAddress().isReachable(Constants.PUBLISHER_HEARTBEAT_TIMEOUT)){
                                System.out.println("Publisher Disconnected");
                                terminate();
                            }
                        } catch (SocketException e){
                            //terminate node if the pipe is broken
                            System.out.println("Publisher Disconnected");
                            terminate();
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(type == Constants.SUBSCRIBER) {
                System.out.println("I am a sub");
            }

        }
    }

    public synchronized void onNewPublishedMessage(Message message) {
        try {
            if (type == Constants.SUBSCRIBER){
                System.out.println("-----------SUB__________");
                for (String subject : message.getSubjectTitles()){
                    if (subjectTitles.contains(subject)){
                        message.getData().put("subject", subject);
                        sendMessage(message.getData());
                        System.out.println(message.getData().toString());
                    }
                }

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
