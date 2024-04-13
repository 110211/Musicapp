package com.huce.hma.presentation.guest.register;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.huce.hma.R;
import com.huce.hma.common.component.BaseActivity;
import com.huce.hma.common.component.LoadingDialog;
import com.huce.hma.common.component.PopupFragment;
import com.huce.hma.common.utils.Status;
import com.huce.hma.databinding.ActivityGuestRegisterBinding;
import com.huce.hma.presentation.guest.login.LoginActivity;

public class RegisterActivity extends BaseActivity {
    private ActivityGuestRegisterBinding binding;
    private RegisterViewModel registerViewModel;
    private PopupFragment popupFragment;
    private LoadingDialog loadingFragment;

    @RequiresApi(api = Build.VERSION_CODES.R)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerViewModel = new ViewModelProvider(this).get(RegisterViewModel.class);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_guest_register);

        binding.setLifecycleOwner(this);

        binding.setRegisterViewModel(registerViewModel);
//        TextView txtBirth = binding.txtViewBirthday;
//        txtBirth.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
//                startActivity(intent);
//            }
//        });

        TextView SignInwithAccount = binding.signUp;
        SignInwithAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });


        registerViewModel.registerResponse.observe(this, resp -> {
            if (resp.getStatus() == 201) {
//                SharedPreferences sharedPreferences = getSharedPreferences(String.valueOf(R.string.app_preference_file_key), Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("jwtToken", resp.getData().getToken());
//                editor.apply();
                popupFragment = new PopupFragment(
                        RegisterActivity.this,
                        PopupFragment.SUCCESS,
                        "Success",
                        resp.getMessage(), new PopupFragment.OnShow() {});
            } else {
                popupFragment = new PopupFragment(
                        RegisterActivity.this,
                        PopupFragment.ERROR,
                        "Error",
                        resp.getMessage(), new PopupFragment.OnShow() {});
            }

            popupFragment.show();
        });

        registerViewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                if (loadingFragment == null) {
                    loadingFragment = new LoadingDialog(RegisterActivity.this);
                }
                loadingFragment.startLoading();
            } else {
                loadingFragment.stopLoading();
            }
        });
        registerstatus();
    }
    private void registerstatus() {
        registerViewModel.getRegisterStatus().observe(this, status -> {
            switch (status) {
                case Status.emptyEmail:
                    //tuong tu cho password
                    binding.editTextTextEmailAddress.setError("Email khôngđ được để trống");
                    binding.editTextTextEmailAddress.requestFocus();
                    break;
                case Status.emptyUsername:
                    //tuong tu cho password
                    binding.editTextTextUsername.setError("Username khôngđ được để trống");
                    binding.editTextTextUsername.requestFocus();
                    break;
                case Status.emptyPassWord:
                    //tuong tu cho password
                    binding.editTextPassword.setError("Password khôngđ được để trống");
                    binding.editTextPassword.requestFocus();
                    break;
                case Status.emptycfPassword:
                    //tuong tu cho password
                    binding.editTextCfPassword.setError("Confirm Password khôngđ được để trống");
                    binding.editTextCfPassword.requestFocus();
                    break;
                case Status.isEmail:
                    binding.editTextTextEmailAddress.setError("Email không đúng định dạng");
                    binding.editTextTextEmailAddress.requestFocus();
                    break;
            }

        });
    }
}
