package com.huce.hma.presentation.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.huce.hma.BuildConfig;
import com.huce.hma.R;
import com.huce.hma.common.component.BaseActivity;
import com.huce.hma.common.component.LoadingDialog;
import com.huce.hma.common.services.JWTTokenManager;
import com.huce.hma.data.local.RoomDB;
import com.huce.hma.databinding.ActivitySplashBinding;
import com.huce.hma.presentation.app.download.DownloadActivity;
import com.huce.hma.presentation.app.home.HomeActivity;
import com.huce.hma.presentation.guest.login.LoginActivity;

import java.io.File;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity {
    private Intent intent;
    private ActivitySplashBinding binding;
    private SplashViewModel splashViewModel;
    private LoadingDialog loadingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        splashViewModel = new ViewModelProvider(this).get(SplashViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash);

        binding.setLifecycleOwner(this);

        binding.setSplashViewModel(splashViewModel);

//        /// init local db
//        RoomDB database = Room.databaseBuilder(this, RoomDB.class, "HMA_DB")
//                .allowMainThreadQueries()
//                .build();

        //         check update
        if (loadingFragment == null) {
            loadingFragment = new LoadingDialog(this);
        }
        loadingFragment.startLoading();
        checkLogin();
//        splashViewModel.checkUpdate();

        splashViewModel.checkUpdateResp.observe(this, resp -> {
            Toast.makeText(this, resp.getMessage(), Toast.LENGTH_SHORT).show();
            if (resp.getStatus() == 200) {
                if (resp.getData().getVersion() > BuildConfig.VERSION_CODE) {
                    String path = getExternalFilesDir(null) + File.separator + resp.getData().getFileName();
                    splashViewModel.downloadUpdate(resp.getData().getId(), path);
                } else {
                    checkLogin();
                }
            } else {
                intent = new Intent(SplashActivity.this, DownloadActivity.class);
                startActivity(intent);
                finish();
            }
        });

        splashViewModel.downloadUpdatePath.observe(this, path -> {
            loadingFragment.stopLoading();
            File apkFile = new File(path);
            Uri uri = FileProvider.getUriForFile(this,
                    getApplicationContext().getPackageName() + ".provider", apkFile);
            intent = new Intent(Intent.ACTION_VIEW);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
            startActivity(intent);

        });

        splashViewModel.loginResp.observe(this, resp -> {
            Toast.makeText(this, resp.getMessage(), Toast.LENGTH_SHORT).show();

            if (resp.getStatus() == 200) {
                intent = new Intent(SplashActivity.this, HomeActivity.class);
            } else {
                JWTTokenManager.saveToken(getApplicationContext(), "");
                Toast.makeText(this, "Vui lòng đăng nhập lại", Toast.LENGTH_SHORT).show();
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }
            startActivity(intent);
            finish();
        });
    }

    public void checkLogin() {
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                String token = JWTTokenManager.getToken(getApplicationContext());
                loadingFragment.stopLoading();
                if (!token.isEmpty()) {
                    splashViewModel.getInfo();
                } else {
                    intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        splashThread.start();
    }
}
