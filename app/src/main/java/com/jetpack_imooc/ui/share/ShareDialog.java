package com.jetpack_imooc.ui.share;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.jetpack.libcommon.utils.EasyLog;
import com.jetpack.libcommon.utils.PixUtils;
import com.jetpack.libcommon.view.CornerFrameLayout;
import com.jetpack.libcommon.view.ViewHelper;
import com.jetpack_imooc.R;
import com.jetpack_imooc.view.PPImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * @author dhl
 * @version V1.0
 * @Title: ShareDialog
 * @Package
 * @Description: ShareDialog
 * @date 2022 0521
 */
public class ShareDialog extends AlertDialog {

    private static final String TAG = "ShareDialog";

    private List<ResolveInfo> shareitems = new ArrayList<>();

    private ShareAdapter shareAdapter;

    private CornerFrameLayout cornerFrameLayout ;
    public ShareDialog(@NonNull Context context) {
        super(context);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        cornerFrameLayout = new CornerFrameLayout(getContext());
        cornerFrameLayout.setBackgroundColor(Color.WHITE);
        cornerFrameLayout.setViewOutline(PixUtils.dp2px(20), ViewHelper.RADIUS_TOP);

        RecyclerView gridView = new RecyclerView(getContext());
        gridView.setLayoutManager(new GridLayoutManager(getContext(),4));
        shareAdapter = new ShareAdapter();
        gridView.setAdapter(shareAdapter);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        int margin = PixUtils.dp2px(20);

        params.leftMargin = params.topMargin = params.rightMargin = params.bottomMargin = margin;
        params.gravity = Gravity.CENTER;
        cornerFrameLayout.addView(gridView, params);
        setContentView(cornerFrameLayout);
        getWindow().setGravity(Gravity.BOTTOM);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        queryShareItems();

    }

    private void queryShareItems(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");

        List<ResolveInfo> resolveInfos = getContext().getPackageManager().queryIntentActivities(intent,0);
        for(ResolveInfo resolveInfo :resolveInfos){
            String packageName = resolveInfo.activityInfo.packageName;
            if(TextUtils.equals(packageName,"com.tencent.mm") || TextUtils.equals(packageName,"com.tencent.mobileqq")){
                shareitems.add(resolveInfo);
            }
        }
        shareAdapter.notifyDataSetChanged();

    }

    private class ShareAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        private final PackageManager packageManager;

        ShareAdapter(){
            packageManager = getContext().getPackageManager();
        }
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_share_item,parent,false);

            return new RecyclerView.ViewHolder(view){};
        }


        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

            ResolveInfo resolveInfo = shareitems.get(position);
            PPImageView imageView = holder.itemView.findViewById(R.id.share_icon);
            Drawable drawable = resolveInfo.loadIcon(packageManager);
            imageView.setImageDrawable(drawable);
            TextView shareText = holder.itemView.findViewById(R.id.share_text);
            shareText.setText(resolveInfo.loadLabel(packageManager));


        }

        @Override
        public int getItemCount() {
            return shareitems.size();
        }
    }
}
