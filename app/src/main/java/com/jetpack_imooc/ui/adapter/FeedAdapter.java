package com.jetpack_imooc.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.jetpack.libcommon.extention.AbsPagedListAdapter;
import com.jetpack.libcommon.extention.LiveDataBus;
import com.jetpack.libcommon.utils.EasyLog;
import com.jetpack_imooc.BR;
import com.jetpack_imooc.R;
import com.jetpack_imooc.databinding.LayoutFeedTypeImageBinding;
import com.jetpack_imooc.databinding.LayoutFeedTypeVideoBinding;
import com.jetpack_imooc.exoplayer.IPlayTarget;
import com.jetpack_imooc.model.Feed;
import com.jetpack_imooc.ui.home.InteractionPresenter;
import com.jetpack_imooc.view.ListPlayView;


/**
 * @author dhl
 * @version V1.0
 * @Title: FeedAdapter
 * @Package FeedAdapter
 * @Description: FeedAdapter
 * @date 2022 0426
 */
public class FeedAdapter extends AbsPagedListAdapter<Feed,FeedAdapter.ViewHolder> {

    private static final String TAG = "FeedAdapter";

    private final LayoutInflater inflate;
    private final Context mContext;
    private final String mCategory;
    public FeedAdapter(Context context, String category) {
        super(new DiffUtil.ItemCallback<Feed>() {
            @Override
            public boolean areItemsTheSame(@NonNull Feed oldItem, @NonNull Feed newItem) {
                return oldItem.id == newItem.id;
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


    @Override
    protected int getItemViewType2(int position) {
        Feed feed = getItem(position);
        switch (feed.itemType){
            case Feed.TYPE_IMAGE_TEXT:
                return R.layout.layout_feed_type_image;
            case Feed.TYPE_VIDEO:
                return R.layout.layout_feed_type_video;
        }
        return 0;
    }

    @Override
    protected ViewHolder onCreateViewHolder2(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(inflate, viewType, parent, false);
        return new ViewHolder(binding.getRoot(), binding);
    }



    @Override
    protected void onBindViewHolder2(ViewHolder holder, int position) {
        final Feed feed = getItem(position);

        holder.bindData(feed);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // FeedDetailActivity.startFeedDetailActivity(mContext, feed, mCategory);
                onStartFeedDetailActivity(feed);
                if (mFeedObserver == null) {
                    EasyLog.e(TAG,"mFeedObserver");
                    mFeedObserver = new FeedObserver();
                    LiveDataBus.get()
                            .with(InteractionPresenter.DATA_FROM_INTERACTION)
                            .observe((LifecycleOwner) mContext, mFeedObserver);
                }
                mFeedObserver.setFeed(feed);
            }
        });



    }

    public void onStartFeedDetailActivity(Feed feed) {

    }

    private FeedObserver mFeedObserver;

    private class FeedObserver implements Observer<Feed> {

        private Feed mFeed;

        @Override
        public void onChanged(Feed newOne) {
            if (mFeed.id != newOne.id)
                return;
            mFeed.author = newOne.author;
            mFeed.ugc = newOne.ugc;
            mFeed.notifyChange();
        }

        public void setFeed(Feed feed) {

            mFeed = feed;
        }
    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private ViewDataBinding mBinding;
        public ListPlayView listPlayView;

        public ViewHolder(View itemView,ViewDataBinding binding){
            super(itemView);
            mBinding = binding;
        }

        public void bindData(Feed item) {
            mBinding.setVariable(BR.feed, item);
            mBinding.setVariable(BR.lifeCycleOwner, mContext);
            if(mBinding instanceof LayoutFeedTypeImageBinding){
                LayoutFeedTypeImageBinding imageBinding = (LayoutFeedTypeImageBinding)mBinding;
                //imageBinding.setFeed(item);
                imageBinding.feedImage.bindData(item.width,item.height,16,item.cover);
            }else{
                LayoutFeedTypeVideoBinding videoBinding = (LayoutFeedTypeVideoBinding)mBinding;
                videoBinding.setFeed(item);
                listPlayView = videoBinding.listPlayerView;
                videoBinding.listPlayerView.bindData(mCategory,item.width,item.height,item.cover,item.url);
            }
        }

        public boolean isVideoItem() {
            return mBinding instanceof LayoutFeedTypeVideoBinding;
        }

        public IPlayTarget getListPlayerView() {
            return listPlayView;
        }
    }

}
