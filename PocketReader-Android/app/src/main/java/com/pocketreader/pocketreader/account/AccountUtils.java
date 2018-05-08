package com.pocketreader.pocketreader.account;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by tony on 5/8/18.
 */

public class AccountUtils {
    public interface OnUserListener {
        void onSuccess(User user);

        void onFail(BmobException e);
    }

    public static boolean check() {
        BmobUser bmobUser = BmobUser.getCurrentUser();
        if (bmobUser != null) {
            // 允许用户使用应用
            return true;
        } else {
            //缓存用户对象为空时， 可打开用户注册界面…
            return false;
        }
    }

    public static void register(User user, final OnUserListener listener) {
        user.signUp(new SaveListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.onSuccess(user);
                    }
                } else {
                    if (listener != null) {
                        listener.onFail(e);
                    }
                }
            }
        });
    }

    public static void login(String account, String pwd, final OnUserListener listener) {
        User user = new User();
        user.setUsername(account);
        user.setPassword(pwd);
        user.login(new SaveListener<User>() {
            @Override
            public void done(User loginUser, BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.onSuccess(loginUser);
                    }
                } else {
                    if (listener != null) {
                        listener.onFail(e);
                    }
                }
            }
        });
    }

    public static void update(User newUser, final OnUserListener listener) {
        BmobUser user = User.getCurrentUser();
        newUser.update(user.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.onSuccess((User) User.getCurrentUser());
                    }
                } else {
                    if (listener != null) {
                        listener.onFail(e);
                    }
                }
            }
        });
    }

    public static void updatePwd(String oldPwd, String newPwd, final OnUserListener listener) {
        User.updateCurrentUserPassword("", "", new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    if (listener != null) {
                        listener.onSuccess((User) User.getCurrentUser());
                    }
                } else {
                    if (listener != null) {
                        listener.onFail(e);
                    }
                }
            }
        });
    }

    public static void logOut() {
        User.logOut();
    }
}
