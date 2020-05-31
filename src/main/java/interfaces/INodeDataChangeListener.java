package interfaces;

import messaging.Message;
import networking.Node;

import java.util.List;

public interface INodeDataChangeListener {
    //void onNewSubscriber(Node node, List<String> subjectTitles);
    void onNewPublisherData(Message data);
}
