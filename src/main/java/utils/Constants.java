package utils;

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

    private Constants(){
        // do not initialize..
    }
}
