package com.pocket.reader.model.dao;

import android.util.Log;

import com.pocket.reader.account.User;
import com.pocket.reader.core.ShareBean;
import com.pocket.reader.event.MessageEvent;
import com.pocket.reader.model.bean.Link;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by tony on 5/9/18.
 */

public class LinkDao {
    public interface OnLinkListener {
        void onSuccess();

        void onFail();
    }

    public static void saveLink(ShareBean shareBean, final OnLinkListener linkListener){
        Link link = new Link();
        link.setAuthor(User.getCurrentUser(User.class));
        link.setContent(shareBean.getHtml());
        link.setIcon(shareBean.getIcon());
        link.setSource(shareBean.getSource());
        link.setThumb(shareBean.getThumb());
        link.setTitle(shareBean.getTitle());
        link.setUrl(shareBean.getOriginalUrl());
        link.setCategory(false);
        BmobACL acl = new BmobACL();
        acl.setPublicReadAccess(false);
        acl.setPublicWriteAccess(false);
        acl.setReadAccess(BmobUser.getCurrentUser(), true); // 设置当前用户可写的权限
        acl.setWriteAccess(BmobUser.getCurrentUser(), true); // 设置当前用户可写的权限
        link.setACL(acl);
        link.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.TYPE_LINK));
                    linkListener.onSuccess();
                } else {
                    linkListener.onFail();
                }
            }
        });
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
        acl.setReadAccess(BmobUser.getCurrentUser(), true); // 设置当前用户可写的权限
        acl.setWriteAccess(BmobUser.getCurrentUser(), true); // 设置当前用户可写的权限
        link.setACL(acl);
        link.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    EventBus.getDefault().post(new MessageEvent(MessageEvent.TYPE_LINK));
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
        query.setLimit(Integer.MAX_VALUE);
        query.addWhereEqualTo("category", false);
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

    public static void queryCollectLink(final OnLinkFindListener listener) {
        BmobQuery<Link> query = new BmobQuery<>();
        query.setLimit(Integer.MAX_VALUE);
        query.addWhereEqualTo("category", true);
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



    public static void collectLink(String id, UpdateListener listener) {
        Link link = new Link();
        link.setValue("category", true);
        link.update(id, listener);
    }

    public static void deleteLink(String id, UpdateListener listener) {
        Link link1 = new Link();
        link1.setObjectId(id);
        link1.delete(listener);
    }
}
