package com.pocket.reader.model.bean;


import com.pocket.reader.account.User;

import cn.bmob.v3.BmobObject;

/**
 * Created by tony on 5/8/18.
 */

public class Comment extends BmobObject {
    private Post post;
    private String content;
    private User author;
}
