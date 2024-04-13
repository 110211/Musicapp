package com.huce.hma.presentation.app.profile.changepassword;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.data.remote.services.Retrofit;
import com.huce.hma.presentation.app.profile.changepassword.dto.ChangePasswordRequestDto;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordViewModel extends AndroidViewModel {
    public String confirmPwd;
    public ChangePasswordRequestDto changePasswordRequestDto = new ChangePasswordRequestDto();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public MutableLiveData<HttpResponse<String>> getOTPResp = new MutableLiveData<>();
    public MutableLiveData<HttpResponse<String>> changePwdResp = new MutableLiveData<>();

    public ChangePasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public void getOTPHandler() {
        this.isLoading.setValue(true);

        Call<HttpResponse<String>> repos = Retrofit.getInstance().RequestAuth(getApplication().getApplicationContext()).GetOTP();

        repos.enqueue(new Callback<HttpResponse<String>>() {

            @Override
            public void onResponse(Call<HttpResponse<String>> call, Response<HttpResponse<String>> response) {
                if (!response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        isLoading.setValue(false);
                        HttpResponse<String> res = gson.fromJson(response.errorBody().charStream(), HttpResponse.class);
                        getOTPResp.setValue(res);
                    } catch (Exception e) {
                        HttpResponse<String> res = new HttpResponse<>();
                        res.setStatus(400);
                        res.setMessage("Gửi mã OTP thất bại, Lỗi:" + e.getMessage());
                        isLoading.setValue(false);
                        getOTPResp.setValue(res);
                    }
                    return;
                }

                HttpResponse<String> post = response.body();
                isLoading.setValue(false);
                getOTPResp.setValue(post);
            }

            @Override
            public void onFailure(Call<HttpResponse<String>> call, Throwable t) {
                Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                HttpResponse<String> res = new HttpResponse<>();
                res.setStatus(400);
                res.setMessage("Kết nối đến máy chủ thất bại.");
                isLoading.setValue(false);
                getOTPResp.setValue(res);
            }
        });
    }

    public void changePwdHandler() {
        if (!Objects.equals(changePasswordRequestDto.getPassword(), confirmPwd)) {
            HttpResponse<String> resp = new HttpResponse<>();
            resp.setMessage("Mật khẩu không trùng khớp");
            changePwdResp.setValue(resp);
        } else {
            this.isLoading.setValue(true);
            Call<HttpResponse<String>> repos = Retrofit.getInstance().RequestAuth(getApplication().getApplicationContext()).ChangePassword(changePasswordRequestDto);

            repos.enqueue(new Callback<HttpResponse<String>>() {

                @Override
                public void onResponse(Call<HttpResponse<String>> call, Response<HttpResponse<String>> response) {
                    if (!response.isSuccessful()) {
                        try {
                            Gson gson = new Gson();
                            isLoading.setValue(false);
                            HttpResponse<String> res = gson.fromJson(response.errorBody().charStream(), HttpResponse.class);
                            changePwdResp.setValue(res);
                        } catch (Exception e) {
                            HttpResponse<String> res = new HttpResponse<>();
                            res.setStatus(400);
                            res.setMessage("Đổi mật khẩu thất bại, Lỗi:" + e.getMessage());
                            isLoading.setValue(false);
                            changePwdResp.setValue(res);
                        }
                        return;
                    }

                    HttpResponse<String> post = response.body();
                    isLoading.setValue(false);
                    changePwdResp.setValue(post);
                }

                @Override
                public void onFailure(Call<HttpResponse<String>> call, Throwable t) {
                    Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                    HttpResponse<String> res = new HttpResponse<>();
                    res.setStatus(400);
                    res.setMessage("Kết nối đến máy chủ thất bại.");
                    isLoading.setValue(false);
                    changePwdResp.setValue(res);
                }
            });
        }
    }
}
