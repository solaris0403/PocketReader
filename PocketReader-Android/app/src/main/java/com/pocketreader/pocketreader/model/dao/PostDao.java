package com.pocketreader.pocketreader.dao;

import com.pocketreader.pocketreader.account.User;
import com.pocketreader.pocketreader.model.bean.Post;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by tony on 5/8/18.
 */

public class PostDao {
    public interface OnPostListener {
        void onSuccess();

        void onFail();
    }

    /**
     * 添加一篇帖子,私有
     *
     * @param post 帖子
     */
    public static void insert(Post post, final OnPostListener listener) {
        BmobACL acl = new BmobACL();
        acl.setPublicReadAccess(false);
        acl.setPublicWriteAccess(false);
        acl.setReadAccess(User.getCurrentUser(User.class), true);
        acl.setWriteAccess(User.getCurrentUser(User.class), true);
        post.setACL(acl);
        post.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    listener.onSuccess();
                } else {
                    listener.onFail();
                }
            }
        });
    }
}
