package com.jetpack_imooc.ui.home;

import android.content.Context;
import android.text.TextUtils;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.alibaba.fastjson.JSONObject;
import com.jetpack.libcommon.extention.LiveDataBus;
import com.jetpack.libcommon.utils.AppGlobals;
import com.jetpack.libnetwork.net.ApiResponse;
import com.jetpack.libnetwork.net.ApiService;
import com.jetpack.libnetwork.net.JsonCallback;
import com.jetpack_imooc.model.Feed;
import com.jetpack_imooc.model.User;
import com.jetpack_imooc.ui.login.UserManager;
import com.jetpack_imooc.ui.share.ShareDialog;


/**
 * @author dhl
 * @version V1.0
 * @Title:
 * @Package $
 * @Description: InteractionPresenter
 * @date 2022 0506
 */
public class InteractionPresenter {

    public static final String DATA_FROM_INTERACTION = "data_from_interaction";

    private static final String URL_TOGGLE_FEED_LIK = "/ugc/toggleFeedLike";

    private static final String URL_TOGGLE_FEED_DISS = "/ugc/dissFeed";

    public static void toggleFeedLike(LifecycleOwner owner, Feed feed) {

        if (!UserManager.get().isLogin()) {
            LiveData<User> loginLiveData = UserManager.get().login(AppGlobals.getApplication());
            loginLiveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleFeedLikeInternal(feed);
                    }
                    loginLiveData.removeObserver(this);
                }


            });

            return;
        } else {
            toggleFeedLikeInternal(feed);
        }


    }

    /**
     * 点赞
     * @param feed
     */
    private static void toggleFeedLikeInternal(Feed feed) {
        ApiService.get(URL_TOGGLE_FEED_LIK)
                .addParam("userId", UserManager.get().getUserId())
                .addParam("itemId", feed.itemId)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        super.onSuccess(response);
                        if (response.body != null) {

                           boolean hasLiked = response.body.getBoolean("hasLiked").booleanValue();
                            feed.getUgc().setHasLiked(hasLiked);
                            LiveDataBus.get().with(DATA_FROM_INTERACTION).postValue(feed);


                        }
                    }


                });
    }

    public static void toggleFeedDiss(LifecycleOwner owner, Feed feed) {
        if (!UserManager.get().isLogin()) {
            LiveData<User> loginLiveData = UserManager.get().login(AppGlobals.getApplication());
            loginLiveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if (user != null) {
                        toggleFeedDissInternal(feed);
                    }
                    loginLiveData.removeObserver(this);
                }


            });

            return;
        }else {
            toggleFeedDissInternal(feed);
        }
    }

    /**
     * FeedDiss 踩 和点赞互斥
     * @param feed
     */
    public static void toggleFeedDissInternal(Feed feed) {
        ApiService.get(URL_TOGGLE_FEED_DISS)
                .addParam("userId", UserManager.get().getUserId())
                .addParam("itemId", feed.itemId)
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        super.onSuccess(response);
                        if (response.body != null) {

                            boolean hasLiked = response.body.getBoolean("hasLiked").booleanValue();
                            feed.ugc.setHasdiss(hasLiked);

                        }
                    }
                });
    }

    public static void openShare(Context context,Feed feed){
        String shareContent = feed.feeds_text;
        if(!TextUtils.isEmpty(feed.url)){
            shareContent = feed.url;
        }else if(TextUtils.isEmpty(feed.cover)){
            shareContent = feed.cover;
        }

        ShareDialog shareDialog = new ShareDialog(context);
        shareDialog.show();
    }



//    private static boolean isLogin(LifecycleOwner owner,Observer<User> observer){
//        if(UserManager.get().isLogin()){
//            return true;
//        }else{
//            LiveData<User> liveData = UserManager.get().login(AppGlobals.getApplication());
//            if(liveData == null){
//
//            }
//        }
//    }


}
