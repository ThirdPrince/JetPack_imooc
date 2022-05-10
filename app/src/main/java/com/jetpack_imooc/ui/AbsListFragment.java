package com.jetpack_imooc.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jetpack.libcommon.view.EmptyView;
import com.jetpack_imooc.R;
import com.jetpack_imooc.databinding.LayoutRefreshViewBinding;
import com.jetpack_imooc.databinding.LayoutRefreshViewBindingImpl;
import com.jetpack_imooc.model.Feed;
import com.jetpack_imooc.viewmodel.AbsViewModel;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @author dhl
 * @version V1.0
 * @Title: AbsListFragment
 * @Package AbsListFragment
 * @Description: AbsListFragment
 * @date 2022 0426
 */
public abstract class AbsListFragment<T, M extends AbsViewModel<T>> extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    private LayoutRefreshViewBinding binding;
    private RecyclerView recyclerView;
    protected SmartRefreshLayout smartRefreshLayout;
    private EmptyView emptyView;

    protected PagedListAdapter<T,RecyclerView.ViewHolder> pagedListAdapter;

    protected M mViewModel;

    protected DividerItemDecoration decoration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = LayoutRefreshViewBindingImpl.inflate(inflater, container, false);
        recyclerView = binding.recyclerView;
        smartRefreshLayout = binding.refreshLayout;
        emptyView = binding.emptyView;
        smartRefreshLayout.setEnableRefresh(true);
        smartRefreshLayout.setEnableLoadMore(true);
        smartRefreshLayout.setOnRefreshListener(this);
        smartRefreshLayout.setOnLoadMoreListener(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAnimation(null);
        pagedListAdapter = getAdapter();
        decoration = new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.list_divider));
        recyclerView.addItemDecoration(decoration);
        recyclerView.setAdapter(pagedListAdapter);
        return binding.getRoot();
    }

    protected abstract void afterView();

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        genericViewModel();
        afterView();

    }

    /**
     * 利用 子类传递的泛型实参实例化出absViewModel 对象
     */
    private void genericViewModel() {
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] arguments = type.getActualTypeArguments();
        if (arguments.length > 1) {
            Type argument = arguments[1];
            Class modelClaz = ((Class) argument).asSubclass(AbsViewModel.class);
            mViewModel = (M) ViewModelProviders.of(this).get(modelClaz);


            mViewModel.getPageData().observe(getViewLifecycleOwner(), new Observer<PagedList<T>>() {
                @Override
                public void onChanged(PagedList<T> ts) {
                    pagedListAdapter.submitList(ts);
                    //smartRefreshLayout.finishRefresh();
                }
            });

            mViewModel.getBoundaryPageData().observe(getViewLifecycleOwner(), aBoolean -> finishRefresh(aBoolean));
        }
    }

    public void submitList(PagedList<T> pageList) {
        if (pageList.size() > 0) {
            pagedListAdapter.submitList(pageList);
        }
        finishRefresh(pageList.size() > 0);
    }

    public void finishRefresh(boolean hasData) {
        PagedList<T> currentList = pagedListAdapter.getCurrentList();
        hasData = hasData || (currentList != null && currentList.size() > 0);
        RefreshState refreshState = smartRefreshLayout.getState();
        if (refreshState.isFooter && refreshState.isOpening) {
            smartRefreshLayout.finishLoadMore();
        } else if (refreshState.isHeader && refreshState.isOpening) {
            smartRefreshLayout.finishRefresh();
        }

        if (hasData) {
            emptyView.setVisibility(View.GONE);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }

    }

    public abstract PagedListAdapter<T, RecyclerView.ViewHolder> getAdapter();


}
