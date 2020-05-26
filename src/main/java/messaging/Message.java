package messaging;

import org.json.JSONObject;

public class Message {
    private long timeStamp;
    private JSONObject data;

    public Message(JSONObject data, long timeStamp){
        this.data = data;
        this.timeStamp = timeStamp;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public JSONObject getData() {
        return data;
    }
}
