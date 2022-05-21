package com.jetpack.libcommon.view;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jetpack.libcommon.R;
import com.jetpack.libcommon.utils.EasyLog;

/**
 * @author dhl
 * @Date 2022 0518
 */
public class WindowInsertNavHostFragment extends NavHostFragment {


    private static final String TAG = "WindowInsertNavHostFrag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        WindowInsertsFrameLayout layout = new WindowInsertsFrameLayout(inflater.getContext());
        layout.setId(getId());
        return layout;
    }
}