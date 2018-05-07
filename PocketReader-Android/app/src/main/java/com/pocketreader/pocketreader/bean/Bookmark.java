package com.pocketreader.pocketreader.bean;

import com.pocketreader.pocketreader.PLog;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by tony on 5/7/18.
 */

public class Bookmark extends BmobObject {
    private String url;
    private String title;
    private int type;
    private int read_state;
    private String auth;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getRead_state() {
        return read_state;
    }

    public void setRead_state(int read_state) {
        this.read_state = read_state;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public static void insert() {
        Bookmark p2 = new Bookmark();
        p2.setTitle("lucky");
        p2.setUrl("北京海淀");
        p2.save(new SaveListener<String>() {
            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    PLog.d("添加数据成功，返回objectId为：" + objectId);
                } else {
                    PLog.d("创建数据失败：" + e.getMessage());
                }
            }
        });
    }

    public static void query() {
        BmobQuery<Bookmark> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject("6b6c11c537", new QueryListener<Bookmark>() {
            @Override
            public void done(Bookmark bookmark, BmobException e) {
                if (e == null) {
                    PLog.d("查询成功");
                } else {
                    PLog.d("查询失败：" + e.getMessage());
                }
            }
        });
    }

    public static void updatez() {
        //更新Person表里面id为6b6c11c537的数据，address内容更新为“北京朝阳”
        final Bookmark p2 = new Bookmark();
        p2.setTitle("北京朝阳  update");
        p2.update("6b6c11c537", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    PLog.d("更新成功:" + p2.getUpdatedAt());
                } else {
                    PLog.d("更新失败：" + e.getMessage());
                }
            }
        });
    }

    public static void deletez(String id) {
        final Bookmark p2 = new Bookmark();
        p2.setObjectId(id);
        p2.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    PLog.d("删除成功:" + p2.getUpdatedAt());
                } else {
                    PLog.d("删除失败：" + e.getMessage());
                }
            }
        });
    }
}
