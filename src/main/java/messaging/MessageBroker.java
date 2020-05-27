package messaging;

import data.Subject;
import networking.Node;
import utils.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageBroker implements Runnable {
    private Map<String, Subject> subjectMap;
    private MessageQueue messageQueue;

    public MessageBroker(){
        this.subjectMap = new HashMap<String, Subject>();
        this.messageQueue = new MessageQueue(Constants.QUEUE_CAPACITY);
    }

    public void addNodeToSubject(Node node, List<String> titles){
        addSubjects(titles);
        for (String title : titles){
            if (subjectMap.containsKey(title)){
                subjectMap.get(title).addNode(node);
            }
        }
    }

    public void publishData(Message data){
        addSubjects(data.getSubjectTitles());
        messageQueue.send(data);
    }

    private void addSubjects(List<String> titles){
        for (String title : titles){
            if (!subjectMap.containsKey(title)){
                Subject subject = new Subject(title);
                subjectMap.put(title, subject);
            }
        }

    }


    public void run() {
        while (true){
            if (!messageQueue.isEmpty()){
                Message message = messageQueue.receive();
                for (String title : message.getSubjectTitles()){
                    if (subjectMap.containsKey(title)){
                        subjectMap.get(title).broadcast(message);
                    }
                }
            }
        }
    }
}
