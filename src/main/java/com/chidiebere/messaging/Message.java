/*
 * Copyright (c) 2020, Chidiebere
 * */

package com.chidiebere.messaging;

import org.json.JSONObject;

import java.util.List;

/**
 * @author Chidiebere Onyedinma
 * **/

public class Message {
    private long timeStamp;
    private JSONObject data;
    private List<String> subjectTitles;

    public Message(JSONObject data, List<String> subjectTitles, long timeStamp){
        this.data = data;
        this.subjectTitles = subjectTitles;
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public JSONObject getData() {
        return data;
    }

    public List<String> getSubjectTitles(){
        return subjectTitles;
    }
}
