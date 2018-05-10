package com.pocketreader.pocketreader.event;

/**
 * Created by tony on 5/10/18.
 */

public class MessageEvent {
    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int type;
    private String message;

    public MessageEvent(int type) {
        this.type = type;
    }

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static final int TYPE_LINK = 1;
}
