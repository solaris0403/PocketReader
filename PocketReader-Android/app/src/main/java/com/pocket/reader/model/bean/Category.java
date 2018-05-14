package com.pocket.reader.model.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by tony on 5/14/18.
 */

public class Category extends BmobObject{
    private Integer id;
    private Integer parentId;
    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }
}
