package com.pocketreader.pocketreader.bean;

import com.pocketreader.pocketreader.PLog;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by tony on 5/8/18.
 */

public class DataOperater {
    public static void insert(Bookmark bookmark) {
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
}
