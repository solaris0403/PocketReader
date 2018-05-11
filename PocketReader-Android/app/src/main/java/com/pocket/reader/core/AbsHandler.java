package com.pocket.reader.core;

/**
 * Created by tony on 5/11/18.
 */

public abstract class AbsHandler {
    protected static final String TAG = AbsHandler.class.getSimpleName();
    protected AbsHandler successor;

    public abstract void handleRequest(ShareBean shareBean);

    public AbsHandler getSuccessor() {
        return successor;
    }

    public void setSuccessor(AbsHandler successor) {
        this.successor = successor;
    }
}
