/*
 * Copyright (c) 2020, Chidiebere
 * */

package com.chidiebere.utils;

/**
 * @author Chidiebere Onyedinma
 * **/
public final class Constants {

    public static final int SUBSCRIBER = 0x231;
    public static final int PUBLISHER = 0x232;
    public static final int NODE_BUS = 0x233;
    public static final int NODE_CLIENT = 0x234;
    public static final int QUEUE_CAPACITY = 50;
    public static final String SUBJECT = "subject";
    public static final String TIME_STAMP = "_timeStamp";
    public static final String DATA = "data";
    public static final int MINIMUM_BUFFER_SIZE = 63;
    public static final int PUBLISHER_HEARTBEAT_TIMEOUT = 120;
    public static final int SUBSCRIBER_HEARTBEAT_TIMEOUT = 2000;
    public static final String TYPE = "type";
    public static final String SUBJECTS = "subjects";
    public static final String PUB = "Publisher";
    public static final String SUB = "Subscriber";

    private Constants(){
        // do not initialize..
    }
}
