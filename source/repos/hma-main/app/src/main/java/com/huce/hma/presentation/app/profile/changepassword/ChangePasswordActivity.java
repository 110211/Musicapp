package com.huce.hma.presentation.app.profile.changepassword;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.huce.hma.R;
import com.huce.hma.common.component.LoadingDialog;
import com.huce.hma.common.component.PopupFragment;
import com.huce.hma.databinding.ActivityAppProfileChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {
    private ActivityAppProfileChangePasswordBinding binding;
    private ChangePasswordViewModel changePasswordViewModel;
    private PopupFragment popupFragment;
    private LoadingDialog loadingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changePasswordViewModel = new ViewModelProvider(this).get(ChangePasswordViewModel.class);

        binding = DataBindingUtil.setContentView(ChangePasswordActivity.this, R.layout.activity_app_profile_change_password);

        binding.setLifecycleOwner(this);

        binding.setChangePasswordViewModel(changePasswordViewModel);

        /// change action bar

        Typeface customFont = ResourcesCompat.getFont(this, R.font.lexenddeca_bold);
        // Create a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Customize the ActionBar (in this case, Toolbar) appearance
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            // Set a custom font for the title
            TextView titleTextView = new TextView(this);
            titleTextView.setText("Đổi mật khẩu");
            titleTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            titleTextView.setTypeface(customFont);
            titleTextView.setTextSize(18);
            getSupportActionBar().setDisplayShowCustomEnabled(true);
            getSupportActionBar().setCustomView(titleTextView);

            // If you want to show the title, you can hide the custom view
            getSupportActionBar().setDisplayShowTitleEnabled(false);

            getSupportActionBar().show();
        }


        changePasswordViewModel.getOTPResp.observe(this, resp -> {
            if (resp.getStatus() == 200) {
                Toast.makeText(this, resp.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                popupFragment = new PopupFragment(
                        ChangePasswordActivity.this,
                        PopupFragment.ERROR,
                        "Error",
                        resp.getMessage(), new PopupFragment.OnShow() {
                });
            }

            popupFragment.show();
        });

        changePasswordViewModel.changePwdResp.observe(this, resp -> {
            if (resp.getStatus() == 200) {
                popupFragment = new PopupFragment(
                        ChangePasswordActivity.this,
                        PopupFragment.SUCCESS,
                        "Success",
                        resp.getMessage(), new PopupFragment.OnShow() {
                });
            } else {
                popupFragment = new PopupFragment(
                        ChangePasswordActivity.this,
                        PopupFragment.ERROR,
                        "Error",
                        resp.getMessage(), new PopupFragment.OnShow() {
                });
            }

            popupFragment.show();
        });


        changePasswordViewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                if (loadingFragment == null) {
                    loadingFragment = new LoadingDialog(ChangePasswordActivity.this);
                }
                loadingFragment.startLoading();
            } else {
                loadingFragment.stopLoading();
            }
        });
    }
}
