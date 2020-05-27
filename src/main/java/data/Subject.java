package data;

import messaging.Message;
import networking.Node;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Subject {
    String title;
    List<Node> nodes;

    public Subject(String title){
        this.title = title;
        nodes = new ArrayList<Node>();
    }

    public String getTitle(){
        return title;
    }

    public boolean addNode(Node node){
        return nodes.add(node);
    }

    public boolean removeNode(Node node){
        return nodes.remove(node);
    }

    public void broadcast(Message message){
        for (Node node : nodes){
            if (node.isConnected()){
                try {
                    node.sendMessage(message.getData());
                } catch (IOException e) {
                    node.removeNode();
                    removeNode(node);
                    e.printStackTrace();
                }
            } else {
                removeNode(node);
            }
        }
    }

}
