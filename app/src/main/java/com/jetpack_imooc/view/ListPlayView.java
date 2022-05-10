package com.jetpack_imooc.view;

import android.content.Context;
import android.media.Image;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.jetpack.libcommon.utils.PixUtils;
import com.jetpack_imooc.R;

/**
 * @author dhl
 * @version V1.0
 * @Title: ListPlayView
 * @Package ListPlayView
 * @Description: ListPlayView
 * @date 2022 0426
 */
public class ListPlayView extends FrameLayout {

    private View bufferView;

    private PPImageView cover, blur, playBtn;

    private String mVideoUrl;

    private String mCategory;

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


}
