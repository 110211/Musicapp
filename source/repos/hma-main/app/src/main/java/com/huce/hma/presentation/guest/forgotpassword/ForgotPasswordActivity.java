package com.huce.hma.presentation.guest.forgotpassword;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.huce.hma.R;
import com.huce.hma.common.component.BaseActivity;
import com.huce.hma.common.component.LoadingDialog;
import com.huce.hma.common.component.PopupFragment;
import com.huce.hma.databinding.ActivityGuestForgotpasswordBinding;
import com.huce.hma.presentation.guest.register.RegisterActivity;


public class ForgotPasswordActivity extends BaseActivity {
    private ActivityGuestForgotpasswordBinding binding;
    private ForgotPasswordViewModel forgotPasswordViewModel;

    private PopupFragment popupFragment;
    private LoadingDialog loadingFragment;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        forgotPasswordViewModel = new ViewModelProvider(this).get(ForgotPasswordViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_forgotpassword);

        binding.setLifecycleOwner(this);

        binding.setForgotPasswordViewModel(forgotPasswordViewModel);


        forgotPasswordViewModel.forgotResponse.observe(this, resp -> {
            if (resp.getStatus() == 200) {
                popupFragment = new PopupFragment(
                        ForgotPasswordActivity.this,
                        PopupFragment.SUCCESS,
                        "Success",
                        resp.getMessage(), new PopupFragment.OnShow() {
                });
            } else {
                popupFragment = new PopupFragment(
                        ForgotPasswordActivity.this,
                        PopupFragment.ERROR,
                        "Error",
                        resp.getMessage(), new PopupFragment.OnShow() {
                });
            }

            popupFragment.show();
        });

        forgotPasswordViewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                if (loadingFragment == null) {
                    loadingFragment = new LoadingDialog(ForgotPasswordActivity.this);
                }
                loadingFragment.startLoading();
            } else {
                loadingFragment.stopLoading();
            }
        });


        forgotPasswordViewModel.registerClick.observe(this, click -> {
            Intent intent = new Intent(ForgotPasswordActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
