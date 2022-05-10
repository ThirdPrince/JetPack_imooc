package com.jetpack_imooc.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * @author dhl
 * @version V1.0
 * @Title: AbsViewModel
 * @Package AbsViewModel
 * @Description: AbsViewModel
 * @date 2022 0426
 */
public abstract class AbsViewModel<T> extends ViewModel {

    private DataSource dataSource;
    protected PagedList.Config config;

    private LiveData<PagedList<T>> pageData;


    private MutableLiveData<Boolean> boundaryPageData = new MutableLiveData<>();

    public AbsViewModel() {
        config = new PagedList.Config.Builder()
                .setPageSize(10)
                .setInitialLoadSizeHint(12)
                //.setEnablePlaceholders(false);
                .build();

        pageData = new LivePagedListBuilder(factory, config)
                .setInitialLoadKey(0)
                .setBoundaryCallback(callback)
                .build();

    }

    public LiveData<PagedList<T>> getPageData() {
        return pageData;
    }

    public DataSource getDataSource() {
        return dataSource;
    }


    public LiveData<Boolean> getBoundaryPageData() {
        return boundaryPageData;
    }


    PagedList.BoundaryCallback callback = new PagedList.BoundaryCallback() {
        @Override
        public void onZeroItemsLoaded() {
            boundaryPageData.postValue(false);
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull Object itemAtFront) {
            boundaryPageData.postValue(true);
        }

        @Override
        public void onItemAtEndLoaded(@NonNull Object itemAtEnd) {
            super.onItemAtEndLoaded(itemAtEnd);
        }
    };
    DataSource.Factory factory = new DataSource.Factory() {
        @NonNull
        @Override
        public DataSource create() {
            if (dataSource == null || dataSource.isInvalid()) {
                dataSource = createDataSource();
            }
            return dataSource;
        }
    };

    public abstract DataSource createDataSource();
}
