package com.pocket.reader.account;

import java.util.List;

import cn.bmob.v3.BmobUser;

/**
 * Created by tony on 5/8/18.
 */

public class User extends BmobUser{
    private List<String> category;

    public List<String> getCategory() {
        return category;
    }

    public void setCategory(List<String> category) {
        this.category = category;
    }
}
