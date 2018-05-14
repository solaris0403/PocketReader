package com.pocket.reader.data;

import com.pocket.reader.model.bean.Link;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by tony on 5/14/18.
 */

public class LinkManager extends Observable {
    private static final LinkManager ourInstance = new LinkManager();

    public static LinkManager getInstance() {
        return ourInstance;
    }

    private LinkManager() {
    }

    private List<Link> sLinks = new ArrayList<>();

    public List<Link> getLinks() {
        return sLinks;
    }

    public void update(List<Link> links) {
        sLinks.clear();
        sLinks.addAll(links);
        notifyObservers();
    }

    @Override
    public void addObserver(Observer observer) {
        super.addObserver(observer);
        observer.update(this, sLinks);
    }
}
