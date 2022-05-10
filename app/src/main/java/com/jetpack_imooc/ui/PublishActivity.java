package com.jetpack_imooc.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.jetpack_imooc.R;
import com.jetpack_imooc.libnavannotation.ActivityDestination;


@ActivityDestination(pageUrl = "main/tabs/publish", asStarter = false)
public class PublishActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pusblish);
    }
}