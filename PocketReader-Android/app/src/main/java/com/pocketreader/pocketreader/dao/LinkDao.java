package com.pocketreader.pocketreader.dao;

import android.util.Log;

import com.pocketreader.pocketreader.account.User;
import com.pocketreader.pocketreader.bean.Link;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by tony on 5/9/18.
 */

public class LinkDao {
    public interface OnLinkListener {
        void onSuccess();

        void onFail();
    }

    /**
     * 保存一个链接
     *
     * @param listener
     */
    public static void saveLink(String url, final OnLinkListener listener) {
        Link link = new Link();
        link.setTitle(url);
        link.setUrl(url);
        link.setAuthor(User.getCurrentUser(User.class));
        link.setContent(url);
        link.setIcon(url);
        link.setSource(url);
        link.setThumb(url);
        BmobACL acl = new BmobACL();
        acl.setPublicReadAccess(false);
        acl.setPublicWriteAccess(false);
        acl.setReadAccess(User.getCurrentUser(User.class), true);
        acl.setWriteAccess(User.getCurrentUser(User.class), true);
        link.setACL(acl);
        link.save(new SaveListener<String>() {
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

    public interface OnLinkFindListener {
        void onSuccess(List<Link> list);

        void onFail();
    }

    public static void queryLinks(final OnLinkFindListener listener) {
        BmobQuery<Link> query = new BmobQuery<>();
//        query.setLimit(Integer.MAX_VALUE);
        query.addWhereRelatedTo("author", new BmobPointer(User.getCurrentUser(User.class)));
        query.findObjects(new FindListener<Link>() {
            @Override
            public void done(List<Link> list, BmobException e) {
                if (e == null) {
                    listener.onSuccess(list);
                } else {
                    listener.onFail();
                }
            }
        });
    }

    public static void queryLink(final OnLinkFindListener listener) {
        BmobQuery<Link> query = new BmobQuery<>();
        query.getObject("977181060a", new QueryListener<Link>() {
            @Override
            public void done(Link link, BmobException e) {
                if (e == null) {
                    List<Link> links = new ArrayList<>();
                    links.add(link);
                    listener.onSuccess(links);
                } else {
                    listener.onFail();
                    Log.e("123", e.getErrorCode()+":"+e.toString());
                }
            }
        });
    }
}
