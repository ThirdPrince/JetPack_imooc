package com.jetpack_imooc.exoplayer;

import android.graphics.Point;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dhl
 * @version V1.0
 * @Title: PageListPlayDetector
 * @Package PageListPlayDetector
 * @Description: PageListPlayDetector
 * @date 2022 0614
 */
public class PageListPlayDetector {

    private List<IPlayTarget> mTargets = new ArrayList<>();

    private IPlayTarget playingTarget;

    public void addTarget(IPlayTarget target){
        mTargets.add(target);
    }

    public void removeTarget(IPlayTarget target){
        mTargets.remove(target);
    }

    private RecyclerView mRecyclerView ;

    public PageListPlayDetector(LifecycleOwner owner, RecyclerView recyclerView){
        mRecyclerView = recyclerView;
        owner.getLifecycle().addObserver(new LifecycleEventObserver() {
            @Override
            public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {

                if(event == Lifecycle.Event.ON_DESTROY){
                 recyclerView.getAdapter().unregisterAdapterDataObserver(dataObserver);
                }
            }
        });

        recyclerView.getAdapter().registerAdapterDataObserver(dataObserver);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(newState == RecyclerView.SCROLL_STATE_IDLE){
                    autoPlay();
                }

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(playingTarget != null && playingTarget.isPlaying() && !isTargetInBounds(playingTarget)){
                    playingTarget.inActive();
                }
            }
        });

    }

    private RecyclerView.AdapterDataObserver dataObserver = new RecyclerView.AdapterDataObserver() {


        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {

              autoPlay();
        }


    };

    private void autoPlay() {
        if (mTargets.size() <= 0 || mRecyclerView.getChildCount()<=0){
            return;
        }
        if(playingTarget != null && playingTarget.isPlaying() && isTargetInBounds(playingTarget)){
            return;
        }

        IPlayTarget activeTarget = null;
        for (IPlayTarget target:mTargets){
           boolean inBounds  =  isTargetInBounds(target);
           if(inBounds){
               activeTarget = target;
               break;
           }
        }
        if(activeTarget!= null){
            if(playingTarget !=null && playingTarget.isPlaying()){
                playingTarget.inActive();
            }
            playingTarget = activeTarget;
            activeTarget.onActive();
        }


    }

    private boolean isTargetInBounds(IPlayTarget target) {
        ViewGroup owner = target.getOwner();
        ensureRecyclerViewLocation();
        if(!owner.isShown() || !owner.isAttachedToWindow() ){
           return false;
        }
        int[] location = new int[2];
        owner.getLocationOnScreen(location);
        int center = location[1] + owner.getHeight() / 2;
        return center >= rvLocation.x && center <= rvLocation.y;

    }

    private Point rvLocation = null;

    private Point ensureRecyclerViewLocation() {
        if (rvLocation == null){
            int[] location = new int[2];
            mRecyclerView.getLocationOnScreen(location);
            int top = location[1];
            int bottom = top + mRecyclerView.getHeight();
            rvLocation = new Point(top,bottom);
        }

        return rvLocation;


    }

    public void onPause() {
        if(playingTarget != null){
            playingTarget.inActive();
        }
    }

    public void onResume(){
        if(playingTarget != null){
            playingTarget.onActive();
        }
    }
}
