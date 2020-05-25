package networking;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;


public class Node {
    private Socket nodeSocket;
    private OutputStream out;
    private InputStream in;
    private InetAddress address;
    private boolean connected;
    private int type;

    public Node(Socket socket){
        this.nodeSocket = socket;

        if (nodeSocket.isConnected()){
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

    public JSONObject receiveMessage() throws IOException {
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

    public void sendMessage(JSONObject json) throws IOException {
        try {
            OutputStreamWriter osw = new OutputStreamWriter(out);
            osw.write(json.toString());
            osw.write(0);
            osw.flush();
        }
        catch (SocketException e){
            removeNode();
            System.out.println("Node has been disconnected");
        }

    }

    public void removeNode() {
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
        return connected;
    }

    public InetAddress getAddress(){
        return address;
    }

    public void setType(int type){
        this.type = type;
    }

}
