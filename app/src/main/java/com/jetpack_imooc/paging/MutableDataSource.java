package com.jetpack_imooc.paging;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author dhl
 * @version V1.0
 * @Title: MutableDataSource
 * @Package MutableDataSource
 * @Description: MutableDataSource
 * @date 2022 0428
 */
public class MutableDataSource<Key,Value> extends PageKeyedDataSource<Key,Value> {

    public List<Value> data = new ArrayList<>();

    public PagedList<Value> buildNewPagedList(PagedList.Config config){

        PagedList<Value> pagedList = new PagedList.Builder<Key,Value>(this,config)
                .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
                .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
                .build();
        return pagedList;
    }


    @Override
    public void loadInitial(@NonNull LoadInitialParams<Key> params, @NonNull LoadInitialCallback<Key, Value> callback) {
        callback.onResult(data,null,null);

    }

    @Override
    public void loadBefore(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Key, Value> callback) {
        callback.onResult(Collections.emptyList(),null);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Key, Value> callback) {
        callback.onResult(Collections.emptyList(),null);
    }
}
