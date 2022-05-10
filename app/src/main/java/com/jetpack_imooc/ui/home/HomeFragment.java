package com.jetpack_imooc.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.jetpack.libcommon.utils.EasyLog;
import com.jetpack_imooc.R;
import com.jetpack_imooc.libnavannotation.FragmentDestination;
import com.jetpack_imooc.model.Feed;
import com.jetpack_imooc.ui.AbsListFragment;
import com.jetpack_imooc.ui.adapter.FeedAdapter;
import com.jetpack_imooc.utils.AppConfig;
import com.scwang.smartrefresh.layout.api.RefreshLayout;

/**
 * 主页
 * @author dhl
 */
@FragmentDestination(pageUrl = "main/tabs/home", asStarter = true)
public class HomeFragment extends AbsListFragment<Feed,HomeViewModel> {


    private static final String TAG = "HomeFragment";

    private String feedType;


    @Override
    protected void afterView() {
        mViewModel.setFeedType(feedType);
        mViewModel.getCacheLiveData().observe(this, new Observer<PagedList<Feed>>() {
            @Override
            public void onChanged(PagedList<Feed> feeds) {
                pagedListAdapter.submitList(feeds);
            }
        });

    }

    @Override
    public PagedListAdapter getAdapter() {

        feedType =  getArguments()== null ? "pics":getArguments().getString("feedType");

        return new FeedAdapter(getContext(),feedType){
            @Override
            public void onViewAttachedToWindow(@NonNull ViewHolder holder) {
                super.onViewAttachedToWindow(holder);
            }
        };
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

        mViewModel.getDataSource().invalidate();

    }
}