package com.pocket.reader.model;

import com.pocket.reader.account.User;
import com.pocket.reader.model.bean.Category;

import cn.bmob.v3.BmobACL;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;



/**
 * Created by tony on 5/14/18.
 */

public class CategoryDao {
    /**
     * 创建一个子分类
     */
    public static void createCategory(String name, Integer parentId, SaveListener<String> listener) {
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        BmobACL acl = new BmobACL();
        acl.setPublicReadAccess(false);
        acl.setPublicWriteAccess(false);
        acl.setReadAccess(User.getCurrentUser(User.class), true);
        acl.setWriteAccess(User.getCurrentUser(User.class), true);
        category.setACL(acl);
        category.save(listener);
    }

    /**
     * 创建一个父分类
     */
    public static void createCategory(String name, SaveListener<String> listener) {
        Category category = new Category();
        category.setParentId(0);
        category.setName(name);
        BmobACL acl = new BmobACL();
        acl.setPublicReadAccess(false);
        acl.setPublicWriteAccess(false);
        acl.setReadAccess(User.getCurrentUser(User.class), true);
        acl.setWriteAccess(User.getCurrentUser(User.class), true);
        category.setACL(acl);
        category.save(listener);
    }

    /**
     * 更新分类名
     *
     * @param objectId
     * @param newName
     * @param listener
     */
    public static void renameCategory(String objectId, String newName, UpdateListener listener) {
        Category category = new Category();
        category.setValue("name", newName);
        category.update(objectId, listener);
    }

    /**
     * 删除分类
     *
     * @param objectId
     * @param listener
     */
    public static void deleteCategory(String objectId, UpdateListener listener) {
        Category category = new Category();
        category.delete(objectId, listener);
    }

    public static void findCategory(FindListener<Category> listener) {
        BmobQuery<Category> bmobQuery = new BmobQuery<>();
        bmobQuery.setLimit(Integer.MAX_VALUE);
        bmobQuery.findObjects(listener);
    }
}
