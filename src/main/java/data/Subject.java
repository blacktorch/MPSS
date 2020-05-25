package data;

import networking.Node;

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
}
