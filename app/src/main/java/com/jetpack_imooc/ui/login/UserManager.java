package com.jetpack_imooc.ui.login;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.jetpack.libnetwork.cache.Cache;
import com.jetpack.libnetwork.cache.CacheManager;
import com.jetpack_imooc.model.User;

/**
 * @author dhl
 * @version V1.0
 * @Title: $
 * @Package $
 * @Description: UserManager
 * @date 2022 0428
 */
public class UserManager {

    private static final String KEY_USER = "cache_user";

    private static UserManager mUserManager;

    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    private User mUser;

    /**
     * 标准写法
     * @return
     */
    public static UserManager get() {
        if (mUserManager == null) {
            synchronized (UserManager.class) {
                if (mUserManager == null) {
                    mUserManager = new UserManager();
                }
            }
        }

        return mUserManager;
    }

    private UserManager() {

        User cache = (User) CacheManager.getCache(KEY_USER);
        if (cache != null) {
            mUser = cache;
        }

    }

    public void save(User user) {
        mUser = user;
        CacheManager.save(KEY_USER, user);
        if (userMutableLiveData.hasObservers()) {
            userMutableLiveData.postValue(user);
        }
    }

    public LiveData<User> login(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
        return userMutableLiveData;

    }

    public boolean isLogin() {
        return mUser != null;
    }

    public User getUser() {
        return mUser;
    }

    public long getUserId() {
        return isLogin() ? mUser.userId : 0;
    }
}
