package com.huce.hma.common.component;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public abstract class BaseActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Ẩn thanh trạng thái
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        // Ẩn action bar nếu bạn đang sử dụng AppCompat
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

    }
}
