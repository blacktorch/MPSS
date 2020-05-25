package messaging;

import data.Subject;
import utils.Constants;

import java.util.HashMap;
import java.util.Map;

public class MessageBroker {
    private Map<String, Subject> subjectMap;
    private MessageQueue messageQueue;

    public MessageBroker(){
        this.subjectMap = new HashMap<String, Subject>();
        this.messageQueue = new MessageQueue(Constants.QUEUE_CAPACITY);
    }
}
