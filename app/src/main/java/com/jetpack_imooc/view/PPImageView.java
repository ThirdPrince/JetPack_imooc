package com.jetpack_imooc.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.jetpack.libcommon.image.ImageSize;
import com.jetpack.libcommon.utils.EasyLog;
import com.jetpack.libcommon.utils.PixUtils;
import com.jetpack.libcommon.utils.WeChatImageUtils;
import com.jetpack.libcommon.view.EmptyView;
import com.jetpack_imooc.R;

import jp.wasabeef.glide.transformations.BlurTransformation;

/**
 * @author dhl
 * @version V1.0
 * @Title: $
 * @Package $
 * @Description: PPImageView
 * @date 2022 0425
 */
public class PPImageView extends AppCompatImageView {

    private static final String TAG = "PPImageView";

    public PPImageView(Context context) {
        this(context,null);
    }

    public PPImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PPImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @BindingAdapter(value = {"image_url","isCircle"})
    public static void setImageUrl(PPImageView view,String imageUrl,boolean isCircle){
        RequestBuilder<Drawable> builder  = Glide.with(view).load(imageUrl).placeholder(R.drawable.image_placeholder);
        if(isCircle){
            builder.transform(new CircleCrop());
        }
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();

        if(layoutParams != null && layoutParams.width > 0 && layoutParams.height >0){
            builder.override(layoutParams.width,layoutParams.height);
        }
        builder.into(view);

    }
    public void bindData(int widthPx, int heightPx, int marginLeft, String imageUrl) {
        bindData(widthPx, heightPx, marginLeft, PixUtils.getScreenWidth(), PixUtils.getScreenWidth(), imageUrl);
    }
    public void bindData(int width,int height,int marginLeft,int maxWidth,int maxHeight,String imageUrl){
        if (width <= 0 || height <= 0) {
            Glide.with(this).load(imageUrl).into(new SimpleTarget<Drawable>() {
                @Override
                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                    int height = resource.getIntrinsicHeight();
                    int width = resource.getIntrinsicWidth();
                    setSize(width, height, marginLeft, maxWidth, maxHeight);

                    setImageDrawable(resource);
                }
            });
            return;
        }

        setSize(width, height, marginLeft, maxWidth, maxHeight);
        setImageUrl(this, imageUrl, false);
    }

    private void setSize(int width,int height,int marginLeft,int maxWidth,int maxHeight){

        int finalWidth,finalHeight;
        if(width > height){
            finalWidth = maxWidth;
            finalHeight = (int)(height / (width *1.0f/ finalWidth));
        }else{
            finalHeight = maxHeight;

            finalWidth = (int)(width /(height*1.0f/ finalHeight));
        }
        //ImageSize imageSize = WeChatImageUtils.getImageSizeByOrgSizeToWeChat(width,height);

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = finalWidth;
        params.height = finalHeight;
        // 设置边距

        if (params instanceof FrameLayout.LayoutParams) {
            ((FrameLayout.LayoutParams) params).leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        } else if (params instanceof LinearLayout.LayoutParams) {
            ((LinearLayout.LayoutParams) params).leftMargin = height > width ? PixUtils.dp2px(marginLeft) : 0;
        }
        setScaleType(ScaleType.CENTER_CROP);
        setLayoutParams(params);

    }


    @BindingAdapter(value = {"blur_url", "radius"})
    public static void setBlurImageUrl(ImageView imageView, String blurUrl, int radius) {
        Glide.with(imageView).load(blurUrl).override(radius)
                .transform(new BlurTransformation())
                .dontAnimate()
                .into(new SimpleTarget<Drawable>() {
                    @Override
                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        imageView.setBackground(resource);
                    }
                });
    }
}
