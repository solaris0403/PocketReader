package com.pocket.reader.model.dao;

import com.pocket.reader.account.User;
import com.pocket.reader.core.ShareBean;
import com.pocket.reader.data.LinkManager;
import com.pocket.reader.event.MessageEvent;
import com.pocket.reader.model.bean.Link;
import com.pocket.reader.utils.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;
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

    public static void saveLink(ShareBean shareBean, final OnLinkListener linkListener) {
        Link link = new Link();
        link.setAuthor(User.getCurrentUser(User.class));
        link.setContent(shareBean.getHtml());
        link.setIcon(shareBean.getIcon());
        link.setSource(shareBean.getSource());
        link.setThumb(shareBean.getThumb());
        link.setTitle(shareBean.getTitle());
        link.setUrl(shareBean.getOriginalUrl());
        link.setCategory(0);
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

    /**
     * 查询到所有当前账号上的链接
     */
    public static void queryLinks() {
        BmobQuery<Link> query = new BmobQuery<>();
        query.setLimit(Integer.MAX_VALUE);
        query.findObjects(new FindListener<Link>() {
            @Override
            public void done(List<Link> list, BmobException e) {
                if (e == null) {
                    LinkManager.getInstance().update(list);
                } else {
                    ToastUtils.show("queryLinks exception");
                }
            }
        });
    }

    /**
     * 查询到所有当前账号上的链接
     */
    public static void queryLinks(FindListener<Link> linkFindListener) {
        BmobQuery<Link> query = new BmobQuery<>();
        query.setLimit(Integer.MAX_VALUE);
        query.findObjects(linkFindListener);
    }

    public static void collectLink(String objId, Integer parentId, UpdateListener listener) {
        Link link = new Link();
        link.setCategory(parentId);
        link.update(objId, listener);
    }

    public static void deleteLink(String objId, UpdateListener listener) {
        Link link1 = new Link();
        link1.setObjectId(objId);
        link1.delete(listener);
    }

    public static void deleteLinks(Integer categoryId) {
        List<BmobObject> links = new ArrayList<>();
        for (Link link : LinkManager.getInstance().getLinks()) {
            if (link.getCategory().intValue() == categoryId.intValue()) {
                links.add(link);
            }
        }
        new BmobBatch().deleteBatch(links).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
            }
        });
    }
}
