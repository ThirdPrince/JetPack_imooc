package com.jetpack_imooc.exoplayer;

import android.view.ViewGroup;

/**
 * @author dhl
 * @version V1.0
 * @Title: IPlayTarget
 * @Package IPlayTarget
 * @Description: IPlayTarget
 * @date 2022 0614
 */
public interface IPlayTarget {
    ViewGroup getOwner();
    void onActive();
    void inActive();
    boolean isPlaying();
}
