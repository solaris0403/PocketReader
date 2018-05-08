package com.pocketreader.pocketreader.bean;

import com.pocketreader.pocketreader.account.User;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * 帖子类
 */

public class Post extends BmobObject {
    private String title;
    private String content;
    private User author;
    private BmobRelation likes;
}
