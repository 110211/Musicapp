package com.huce.hma.presentation.guest.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.huce.hma.R;
import com.huce.hma.common.component.BaseActivity;
import com.huce.hma.common.component.LoadingDialog;
import com.huce.hma.common.component.PopupFragment;
import com.huce.hma.common.services.JWTTokenManager;
import com.huce.hma.common.utils.GoogleSignInUtils;
import com.huce.hma.databinding.ActivityGuestLoginBinding;
import com.huce.hma.presentation.app.home.HomeActivity;
import com.huce.hma.presentation.app.profile.changepassword.ChangePasswordActivity;
import com.huce.hma.presentation.guest.forgotpassword.ForgotPasswordActivity;
import com.huce.hma.presentation.guest.register.RegisterActivity;

public class LoginActivity extends BaseActivity {
    private ActivityGuestLoginBinding binding;
    private LoginViewModel loginViewModel;
    private PopupFragment popupFragment;
    private LoadingDialog loadingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("874142417274-mjtuot81ggbkv1f6ta9qs2kv6qu1n4eq.apps.googleusercontent.com")
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                        GoogleSignInAccount account = GoogleSignInUtils.getGoogleSignInAccountFromTask(task);
                        if (account != null) {
                            loginViewModel.googleLoginHandler(account.getIdToken());
                        } else {
                            popupFragment = new PopupFragment(
                                    LoginActivity.this,
                                    PopupFragment.ERROR,
                                    "Thất bại",
                                    "Đăng nhập Google thất bại. Bạn hãy thử đăng nhập bằng email!", new PopupFragment.OnShow() {
                            });
                            popupFragment.show();
                        }
                    }
                }
        );


        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding = DataBindingUtil.setContentView(LoginActivity.this, R.layout.activity_guest_login);

        binding.setLifecycleOwner(this);

        binding.setLoginViewModel(loginViewModel);


        TextView buttonSignUp = binding.button3;
        Button buttonForgotPassword = binding.forgotpasswordButton;
        Button buttonGoogleLogin = binding.logingg;
        buttonGoogleLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                launcher.launch(signInIntent);
            }
        });


        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        buttonForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });
        loginViewModel.loginResponse.observe(this, resp -> {
            if (resp.getStatus() == 200 || resp.getStatus() == 201) {
                JWTTokenManager.saveToken(getApplicationContext(), resp.getData().getToken());
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            } else {
                popupFragment = new PopupFragment(
                        LoginActivity.this,
                        PopupFragment.ERROR,
                        "Error",
                        resp.getMessage(), new PopupFragment.OnShow() {
                });
                popupFragment.show();
            }
        });

        loginViewModel.isLoading.observe(this, isLoading -> {
            if (isLoading) {
                if (loadingFragment == null) {
                    loadingFragment = new LoadingDialog(LoginActivity.this);
                }
                loadingFragment.startLoading();
            } else {
                loadingFragment.stopLoading();
            }
        });
    }


}
