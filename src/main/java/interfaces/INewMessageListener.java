package interfaces;

import messaging.Message;

public interface INewMessageListener {
    void onNewPublishedMessage(Message message);
}
