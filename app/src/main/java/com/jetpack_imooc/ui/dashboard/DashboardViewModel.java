package com.jetpack_imooc.ui.dashboard;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;

import com.jetpack_imooc.model.Ugc;
import com.jetpack_imooc.viewmodel.AbsViewModel;

public class DashboardViewModel extends AbsViewModel<Ugc> {

    private MutableLiveData<String> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    @Override
    public DataSource createDataSource() {
        return null;
    }

    public LiveData<String> getText() {
        return mText;
    }
}