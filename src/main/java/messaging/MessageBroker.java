package messaging;

import interfaces.INewMessageListener;
import networking.Node;
import utils.Constants;

public class MessageBroker implements Runnable {
    //private Map<String, Subject> subjectMap;
    private MessageQueue messageQueue;
    private INewMessageListener listener;
   // Node node;

    public MessageBroker(INewMessageListener listener){
        //this.subjectMap = new HashMap<String, Subject>();
        this.messageQueue = new MessageQueue(Constants.QUEUE_CAPACITY);
        this.listener = listener;
        //this.node = node;
    }

//    public void addNodeToSubject(Node node, List<String> titles){
//        addSubjects(titles);
//        for (String title : titles){
//            if (subjectMap.containsKey(title)){
//                subjectMap.get(title).addNode(node);
//            }
//        }
//    }

    public synchronized void publishData(Message data){
        //addSubjects(data.getSubjectTitles());
        messageQueue.send(data);
    }

//    private void addSubjects(List<String> titles){
//        for (String title : titles){
//            if (!subjectMap.containsKey(title)){
//                Subject subject = new Subject(title);
//                subjectMap.put(title, subject);
//            }
//        }
//
//    }


    public void run() {
        while (true){
            if (!messageQueue.isEmpty()){
                Message message = messageQueue.receive();
                //System.out.println(message.getData().toString());
                listener.onNewPublishedMessage(message);
            }
        }
    }
}
