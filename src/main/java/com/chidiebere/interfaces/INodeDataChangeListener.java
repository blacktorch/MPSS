package com.chidiebere.interfaces;

import com.chidiebere.messaging.Message;

public interface INodeDataChangeListener {
    void onNewPublisherData(Message data);
}
