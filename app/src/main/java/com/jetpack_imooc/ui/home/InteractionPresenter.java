package com.jetpack_imooc.ui.home;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.jetpack.libcommon.utils.AppGlobals;
import com.jetpack.libnetwork.net.ApiResponse;
import com.jetpack.libnetwork.net.ApiService;
import com.jetpack.libnetwork.net.JsonCallback;
import com.jetpack_imooc.model.Feed;
import com.jetpack_imooc.model.User;
import com.jetpack_imooc.ui.login.UserManager;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author dhl
 * @version V1.0
 * @Title:
 * @Package $
 * @Description: InteractionPresenter
 * @date 2022 0506
 */
public class InteractionPresenter {

    private static final String URL_TOGGLE_FEED_LIK = "/ugc/toggleFeedLike";

    private static final String  URL_TOGGLE_FEED_DISS = "/ugc/dissFeed";

    public static void toggleFeedLike(LifecycleOwner owner,Feed feed){

        if(!UserManager.get().isLogin()){
            LiveData<User> loginLiveData = UserManager.get().login(AppGlobals.getApplication());
            loginLiveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if(user != null){
                        toggleFeedLikeInternal(feed);
                    }
                    loginLiveData.removeObserver(this);
                }


            });

            return;
        }


    }

    private static void toggleFeedLikeInternal(Feed feed) {
        ApiService.get(URL_TOGGLE_FEED_LIK)
                .addParam("userId", UserManager.get().getUserId())
                .addParam("itemId",feed.getItemId())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response)  {
                        super.onSuccess(response);
                        if(response.body != null){
                            try {
                                boolean hasLiked = response.body.getBoolean("hasLiked");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }


                });
    }

    public static void toggleFeedDiss(LifecycleOwner owner,Feed feed){
        if(!UserManager.get().isLogin()){
            LiveData<User> loginLiveData = UserManager.get().login(AppGlobals.getApplication());
            loginLiveData.observe(owner, new Observer<User>() {
                @Override
                public void onChanged(User user) {
                    if(user != null){
                        toggleFeedDissInternal(feed);
                    }
                    loginLiveData.removeObserver(this);
                }


            });

            return;
        }
    }

    public static void toggleFeedDissInternal(Feed feed){
        ApiService.get(URL_TOGGLE_FEED_DISS)
                .addParam("userId",UserManager.get().getUserId())
                .addParam("itemId",feed.getItemId())
                .execute(new JsonCallback<JSONObject>() {
                    @Override
                    public void onSuccess(ApiResponse<JSONObject> response) {
                        super.onSuccess(response);
                        if(response.body != null){
                            try {
                                boolean hasLiked = response.body.getBoolean("hasLiked");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                });
    }
}
