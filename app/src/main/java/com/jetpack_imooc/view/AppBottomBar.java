package com.jetpack_imooc.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;
import com.jetpack_imooc.R;
import com.jetpack_imooc.model.BottomBar;
import com.jetpack_imooc.model.Destination;
import com.jetpack_imooc.model.Tab;
import com.jetpack_imooc.utils.AppConfig;

import java.util.List;

/**
 * @author dhl
 * @version V1.0
 * @Title: AppBottomBar
 * @Package
 * @Description: AppBottomBar
 * @date 2022 0424
 */
public class  AppBottomBar extends BottomNavigationView {


    /**
     * 底部icon
     */
    private  int[] sIcons = new int[]{
            R.drawable.ic_home_black_24dp,R.drawable.icon_tab_sofa,R.drawable.icon_tab_publish,R.drawable.icon_tab_find,R.drawable.icon_tab_mine
    };

    public AppBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        BottomBar bottomBar = AppConfig.getSBottomBar();
        List<Tab> bottomBarTabs = bottomBar.getTabs();
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};

        int[] colors = new int[]{Color.parseColor(bottomBar.getActiveColor()), Color.parseColor(bottomBar.getInActiveColor())};
        ColorStateList colorStateList = new ColorStateList(states, colors);
        setItemIconTintList(colorStateList);
        setItemTextColor(colorStateList);
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        setSelectedItemId(bottomBar.getSelectTab());
        for (int i = 0; i < bottomBarTabs.size(); i++) {
            Tab tab = bottomBarTabs.get(i);
            if (!tab.getEnable()) {
                continue;
            }
            int itemId = getMenuId(tab.getPageUrl());
            if (itemId < 0) {
                continue;
            }
            MenuItem item =  getMenu().add(0, itemId, tab.getIndex(), tab.getTitle());
            item.setIcon(sIcons[tab.getIndex()]);
        }

        for (int i = 0;i < bottomBarTabs.size();i++){
            Tab tab = bottomBarTabs.get(i);
            int iconSize = dp2px(tab.getSize());
            BottomNavigationMenuView bottomNavigationMenuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView bottomNavigationItemView = (BottomNavigationItemView)bottomNavigationMenuView.getChildAt(i);
            bottomNavigationItemView.setIconSize(iconSize);
            if(TextUtils.isEmpty(tab.getTitle())){
                bottomNavigationItemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.getTintColor())));
                bottomNavigationItemView.setShifting(false);
            }


        }


    }

    private int dp2px(int size) {
        float value = getContext().getResources().getDisplayMetrics().density*size +0.5f;
        return (int)value;
    }

    private int getMenuId(String pageUrl) {
        Destination destination = AppConfig.getSDesConfig().get(pageUrl);
        if (destination == null) {
            return -1;
        }
        return destination.getId();
    }
}
