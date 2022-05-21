package com.jetpack_imooc.ui.home;

import android.content.ClipData;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

import com.google.gson.Gson;
import com.jetpack.libcommon.utils.EasyLog;
import com.jetpack.libnetwork.net.ApiResponse;
import com.jetpack.libnetwork.net.ApiService;
import com.jetpack.libnetwork.net.JsonCallback;
import com.jetpack.libnetwork.net.Request;
import com.jetpack_imooc.model.Feed;
import com.jetpack_imooc.paging.MutableDataSource;
import com.jetpack_imooc.viewmodel.AbsViewModel;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.internal.TypeReference;


/**
 * @author dhl
 * @Date 2022 0426
 */
public class HomeViewModel extends AbsViewModel<Feed> {

    private static final String TAG = "HomeViewModel";

    private static final String FEED_HOT_LIST = "/feeds/queryHotFeedsList";

    private String mFeedType;

    private MutableLiveData<PagedList<Feed>> cacheLiveData = new MutableLiveData<>();

    public MutableLiveData<PagedList<Feed>> getCacheLiveData() {
        return cacheLiveData;
    }


    public void setFeedType(String feedType) {

        mFeedType = feedType;
    }

    private MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    @Override
    public DataSource createDataSource() {

        return new FeedDataSource();
    }

    public LiveData<String> getText() {
        return mText;
    }

    class FeedDataSource extends ItemKeyedDataSource<Integer, Feed> {

        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {

            EasyLog.d(TAG, "loadInitial");
            loadData(0, params.requestedLoadSize, callback);
        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            EasyLog.d(TAG, "loadAfter");
            loadData(params.key, params.requestedLoadSize, callback);
        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            EasyLog.d(TAG, "loadAfter");
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            return item.id;
        }
    }

    private void loadData(int key, int count, ItemKeyedDataSource.LoadCallback<Feed> callback) {


        Request request = ApiService.get(FEED_HOT_LIST)
                .addParam("feedType", mFeedType)
                .addParam("userId", 0)
                .addParam("feedId", key)
                .addParam("pageCount", count);

        request.cacheStrategy(Request.NET_CACHE);

        request.execute(new JsonCallback<List<Feed>>() {
            @Override
            public void onSuccess(ApiResponse response) {

                List<Feed> feedList = (List<Feed>) response.body;
                callback.onResult(feedList);
                if (key > 0) {
                    ((MutableLiveData) getBoundaryPageData()).postValue(feedList.size() > 0);
                }
            }

            @Override
            public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                if (response.body != null) {
                    MutableDataSource dataSource = new MutableDataSource<Integer, Feed>();
                    dataSource.data.addAll(response.body);
                    PagedList pagedList = dataSource.buildNewPagedList(config);
                    cacheLiveData.postValue(pagedList);
                }


            }
        });


    }
}