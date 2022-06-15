package com.jetpack_imooc.view;

import android.content.Context;
import android.media.Image;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.jetpack.libcommon.utils.PixUtils;
import com.jetpack_imooc.R;
import com.jetpack_imooc.exoplayer.IPlayTarget;
import com.jetpack_imooc.exoplayer.PageListPlay;
import com.jetpack_imooc.exoplayer.PageListPlayManager;

/**
 * @author dhl
 * @version V1.0
 * @Title: ListPlayView
 * @Package ListPlayView
 * @Description: ListPlayView
 * @date 2022 0426
 */
public class ListPlayView extends FrameLayout implements IPlayTarget, PlayerControlView.VisibilityListener,Player.EventListener{

    private View bufferView;

    private PPImageView cover, blur, playBtn;

    private String mVideoUrl;

    private String mCategory;

    private boolean isPlaying;

    public ListPlayView(@NonNull Context context) {
        this(context, null);
    }

    public ListPlayView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ListPlayView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_play_view, this, true);
        bufferView = findViewById(R.id.buffer_view);
        cover = findViewById(R.id.cover);
        blur = findViewById(R.id.blur_background);
        playBtn = findViewById(R.id.play_btn);
        playBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying()){
                    inActive();
                }else {
                    onActive();
                }
            }
        });

    }

    public void bindData(String category, int width, int height, String coverUrl, String videoUrl) {

        mCategory = category;
        mVideoUrl = videoUrl;
        cover.setImageUrl(cover, coverUrl, false);
        if (width < height) {
            blur.setBlurImageUrl(blur, coverUrl, 5);
            blur.setVisibility(View.VISIBLE);
        } else {
            blur.setVisibility(View.INVISIBLE);
        }

        setSize(width, height);

    }

    /**
     *
     * @param width
     * @param height
     */
    protected void setSize(int width, int height) {
        int maxWidth = PixUtils.getScreenWidth();
        int maxHeight = maxWidth;

        int layoutWidth = maxWidth;
        int layoutHeight = 0;

        int coverWidth;
        int coverHeight;
        if (width >= height) {
            coverWidth = maxWidth;
            layoutHeight = coverHeight = (int) (height / (width * 1.0f / maxWidth));

        } else {
            layoutHeight = coverHeight = maxHeight;
            coverWidth = (int) (width / (height * 1.0f / maxHeight));
        }
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = layoutWidth ;
        params.height = layoutHeight;
        setLayoutParams(params);

        ViewGroup.LayoutParams blurParams = blur.getLayoutParams();

        blurParams.width = layoutWidth;
        blurParams.height = layoutHeight;
        blur.setLayoutParams(blurParams);


        FrameLayout.LayoutParams coverParams = (LayoutParams) cover.getLayoutParams();
        coverParams.width = coverWidth;
        coverParams.height = coverHeight;
        coverParams.gravity = Gravity.CENTER;
        cover.setLayoutParams(coverParams);

    }


    @Override
    public ViewGroup getOwner() {
        return this;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
         PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
         pageListPlay.controlView.show();
         return true;
    }

    @Override
    public void onActive() {
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        PlayerView playerView = pageListPlay.playerView;
        PlayerControlView playerControlView = pageListPlay.controlView;
        SimpleExoPlayer simpleExoPlayer = pageListPlay.exoPlayer;

        ViewParent parent = playerView.getParent();
        if(parent != this){
            if(parent != null){
                ((ViewGroup)parent).removeView(playerView);
            }
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            this.addView(playerView,1,layoutParams);
        }
        ViewParent ctrlParent = playerControlView.getParent();
        if(ctrlParent != this){
            if(ctrlParent != null){
                ((ViewGroup)ctrlParent).removeView(playerControlView);
            }
            FrameLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.BOTTOM;
            this.addView(playerControlView,params);
            playerControlView.setVisibilityListener(this);
        }
        playerControlView.show();

        if(TextUtils.equals(pageListPlay.playerUrl,mVideoUrl)){

        }else {
            MediaSource mediaSource = PageListPlayManager.createMediaSource(mVideoUrl);
            simpleExoPlayer.prepare(mediaSource);
            simpleExoPlayer.setRepeatMode(Player.REPEAT_MODE_ONE);
            simpleExoPlayer.addListener(this);
        }


    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        isPlaying = false;
        bufferView.setVisibility(GONE);
        cover.setVisibility(VISIBLE);
        playBtn.setVisibility(VISIBLE);
        playBtn.setImageResource(R.drawable.icon_video_play);
    }

    @Override
    public void inActive() {
        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        pageListPlay.exoPlayer.setPlayWhenReady(false);
        playBtn.setVisibility(VISIBLE);
        playBtn.setImageResource(R.drawable.icon_video_play);
    }

    @Override
    public boolean isPlaying() {
        return isPlaying;
    }

    @Override
    public void onVisibilityChange(int visibility) {

        playBtn.setVisibility(visibility);
        playBtn.setImageResource(isPlaying ?R.drawable.icon_video_pause:R.drawable.icon_video_play);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        PageListPlay pageListPlay = PageListPlayManager.get(mCategory);
        SimpleExoPlayer exoPlayer = pageListPlay.exoPlayer;


        if(playbackState == Player.STATE_READY && exoPlayer.getBufferedPosition()!=0){
            cover.setVisibility(View.INVISIBLE);
            bufferView.setVisibility(INVISIBLE);
        }else if(playbackState == Player.STATE_BUFFERING) {
             bufferView.setVisibility(VISIBLE);
        }
        isPlaying = playbackState ==  Player.STATE_READY && exoPlayer.getBufferedPosition()!=0 && playWhenReady;

        playBtn.setImageResource(isPlaying?R.drawable.icon_video_pause:R.drawable.icon_video_play);
        exoPlayer.setPlayWhenReady(true);
        }

    }

