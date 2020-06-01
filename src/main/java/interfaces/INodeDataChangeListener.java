package interfaces;

import messaging.Message;

public interface INodeDataChangeListener {
    void onNewPublisherData(Message data);
}
