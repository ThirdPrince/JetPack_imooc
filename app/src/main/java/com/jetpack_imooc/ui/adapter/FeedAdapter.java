package com.jetpack_imooc.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jetpack.libcommon.utils.EasyLog;
import com.jetpack_imooc.databinding.LayoutFeedTypeImageBinding;
import com.jetpack_imooc.databinding.LayoutFeedTypeVideoBinding;
import com.jetpack_imooc.model.Feed;
import com.jetpack_imooc.model.FeedKt;

/**
 * @author dhl
 * @version V1.0
 * @Title: FeedAdapter
 * @Package FeedAdapter
 * @Description: FeedAdapter
 * @date 2022 0426
 */
public class FeedAdapter extends PagedListAdapter<Feed,FeedAdapter.ViewHolder> {

    private static final String TAG = "FeedAdapter";

    private final LayoutInflater inflate;
    private final Context mContext;
    private final String mCategory;
    public FeedAdapter(Context context, String category) {
        super(new DiffUtil.ItemCallback<Feed>() {
            @Override
            public boolean areItemsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        inflate = LayoutInflater.from(context);
        mCategory =  category;
    }

    @NonNull
    @Override
    public FeedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        EasyLog.d(TAG,"onCreateViewHolder");
        ViewDataBinding binding;
        if(viewType == FeedKt.TYPE_IMAGE_TEXT){
             binding = LayoutFeedTypeImageBinding.inflate(inflate);
        }else{
            binding = LayoutFeedTypeVideoBinding.inflate(inflate);
        }
        return new ViewHolder(binding.getRoot(),binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedAdapter.ViewHolder holder, int position) {

        holder.bindData(getItem(position));
    }

    @Override
    public int getItemViewType(int position) {
        Feed feed = getItem(position);
        return feed.getItemType();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ViewDataBinding mBinding;

        public ViewHolder(View itemView,ViewDataBinding binding){
            super(itemView);
            mBinding = binding;
        }

        public void bindData(Feed item) {
            if(mBinding instanceof LayoutFeedTypeImageBinding){
                LayoutFeedTypeImageBinding imageBinding = (LayoutFeedTypeImageBinding)mBinding;
                imageBinding.setFeed(item);
                imageBinding.feedImage.bindData(item.getWidth(),item.getHeight(),16,item.getCover());
            }else{
                LayoutFeedTypeVideoBinding videoBinding = (LayoutFeedTypeVideoBinding)mBinding;
                videoBinding.setFeed(item);
                videoBinding.listPlayerView.bindData(mCategory,item.getWidth(),item.getHeight(),item.getCover(),item.getUrl());
            }
        }
    }

}
