package com.jetpack_imooc.exoplayer;

import android.app.Application;
import android.provider.Settings;
import android.view.LayoutInflater;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.jetpack.libcommon.utils.AppGlobals;
import com.jetpack_imooc.R;

/**
 * @author dhl
 * @version V1.0
 * @Title: $
 * @Package $
 * @Description: PageListPlay
 * @date 2022 0614
 */
public class PageListPlay {

    public SimpleExoPlayer exoPlayer;
    public PlayerView playerView;
    public PlayerControlView controlView;
    public String playerUrl;

    public PageListPlay(){
        Application application = AppGlobals.getApplication();
        exoPlayer = ExoPlayerFactory.newSimpleInstance(application,
                new DefaultRenderersFactory(application),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        playerView = (PlayerView) LayoutInflater.from(application).inflate(R.layout.layout_exo_player_view,null);
        controlView = (PlayerControlView) LayoutInflater.from(application).inflate(R.layout.layout_exo_player_contorller_view,null);
        playerView.setPlayer(exoPlayer);
        controlView.setPlayer(exoPlayer);

    }

    public void release(){
        if(exoPlayer != null){
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop(true);
            exoPlayer.release();
            exoPlayer = null;
        }
        if(playerView != null){
            playerView.setPlayer(null);
            playerView = null;
        }

        if(controlView != null){
            controlView.setPlayer(null);
            controlView.setVisibilityListener(null);
            controlView = null;
        }
    }
}
