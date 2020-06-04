/*
 * Copyright (c) 2020, Chidiebere
 * */

package com.chidiebere.interfaces;

import com.chidiebere.messaging.Message;

/**
 * The {@code INewMessageListener} interface is used to implement a listener
 * to listen for when new messages are published, and the messages are passed
 * to the subscriber
 *
 * @author Chidiebere Onyedinma
 * **/
public interface INewMessageListener {

    void onNewPublishedMessage(Message message);
}
