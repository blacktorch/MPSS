package com.chidiebere.interfaces;

import com.chidiebere.messaging.Message;

public interface INewMessageListener {
    void onNewPublishedMessage(Message message);
}
