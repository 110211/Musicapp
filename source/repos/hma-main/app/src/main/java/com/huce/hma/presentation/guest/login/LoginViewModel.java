package com.huce.hma.presentation.guest.login;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.data.remote.services.Retrofit;
import com.huce.hma.presentation.guest.login.dto.LoginGoogleRequestDTO;
import com.huce.hma.presentation.guest.login.dto.LoginRequestDTO;
import com.huce.hma.presentation.guest.login.dto.LoginResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {
    public LoginRequestDTO user = new LoginRequestDTO();
    public MutableLiveData<HttpResponse<LoginResponseDTO>> loginResponse = new MutableLiveData<HttpResponse<LoginResponseDTO>>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public void loginHandler() {
        this.isLoading.setValue(true);
        Call<HttpResponse<LoginResponseDTO>> repos = Retrofit.getInstance().Request().Login(user);

        repos.enqueue(new Callback<HttpResponse<LoginResponseDTO>>() {
            @Override
            public void onResponse(Call<HttpResponse<LoginResponseDTO>> call, Response<HttpResponse<LoginResponseDTO>> response) {
                if (!response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        HttpResponse<LoginResponseDTO> res = gson.fromJson(response.errorBody().charStream(), HttpResponse.class);
                        isLoading.setValue(false);
                        loginResponse.setValue(res);
                    } catch (Exception e) {
                        HttpResponse<LoginResponseDTO> res = new HttpResponse<>();
                        res.setStatus(400);
                        res.setMessage("Đăng nhập thất bại, Lỗi: " + e.getMessage());
                        isLoading.setValue(false);
                        loginResponse.setValue(res);
                    }

                    return;
                }

                HttpResponse<LoginResponseDTO> post = response.body();
                isLoading.setValue(false);
                loginResponse.setValue(post);
            }

            @Override
            public void onFailure(Call<HttpResponse<LoginResponseDTO>> call, Throwable t) {
                // Xử lý khi request thất bại
                Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                HttpResponse<LoginResponseDTO> res = new HttpResponse<>();
                res.setStatus(400);
                res.setMessage("Thất bại");
                isLoading.setValue(false);
                loginResponse.setValue(res);
            }
        });
    }

    public void googleLoginHandler(String token) {
        this.isLoading.setValue(true);
        LoginGoogleRequestDTO loginGoogleRequestDTO = new LoginGoogleRequestDTO(token);
        Call<HttpResponse<LoginResponseDTO>> repos = Retrofit.getInstance().Request().LoginGoogle(loginGoogleRequestDTO);

        repos.enqueue(new Callback<HttpResponse<LoginResponseDTO>>() {
            @Override
            public void onResponse(Call<HttpResponse<LoginResponseDTO>> call, Response<HttpResponse<LoginResponseDTO>> response) {
                if (!response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        HttpResponse<LoginResponseDTO> res = gson.fromJson(response.errorBody().charStream(), HttpResponse.class);
                        isLoading.setValue(false);
                        loginResponse.setValue(res);
                    } catch (Exception e) {
                        HttpResponse<LoginResponseDTO> res = new HttpResponse<>();
                        res.setStatus(400);
                        res.setMessage("Đăng nhập thất bại, Lỗi: " + e.getMessage());
                        isLoading.setValue(false);
                        loginResponse.setValue(res);
                    }

                    return;
                }

                HttpResponse<LoginResponseDTO> post = response.body();
                isLoading.setValue(false);
                loginResponse.setValue(post);
            }

            @Override
            public void onFailure(Call<HttpResponse<LoginResponseDTO>> call, Throwable t) {
                // Xử lý khi request thất bại
                Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                HttpResponse<LoginResponseDTO> res = new HttpResponse<>();
                res.setStatus(400);
                res.setMessage("Thất bại");
                isLoading.setValue(false);
                loginResponse.setValue(res);
            }
        });
    }


}
