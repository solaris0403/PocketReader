package com.pocket.reader.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by tony on 5/14/18.
 */

public class Category extends BmobObject{
    private int id;
    private String name;
    private int parentId;

    public int getId() {
        return id;
    }

//    public void setId(int id) {
//        this.id = id;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }
}
