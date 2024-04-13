package com.huce.hma.presentation.guest.register;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.huce.hma.common.utils.Status;
import com.huce.hma.common.validator.ValidatorUtils;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.data.remote.services.Retrofit;
import com.huce.hma.presentation.guest.register.dto.RegisterRequestDTO;
import com.huce.hma.presentation.guest.register.dto.RegisterResponseDTO;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterViewModel extends ViewModel {

    public RegisterRequestDTO user = new RegisterRequestDTO();
    public MutableLiveData<Integer> getRegisterStatus() {
        return RegisterStatus;
    }
    private MutableLiveData<Integer> RegisterStatus = new MutableLiveData<>();
    public MutableLiveData<HttpResponse<RegisterResponseDTO>> registerResponse = new MutableLiveData<HttpResponse<RegisterResponseDTO>>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    public String confirmPassword;
    public void registerHandler() {
        //check confirm password
        if (user.getPassword() == null || !user.getPassword().equals(confirmPassword)) {
            HttpResponse<RegisterResponseDTO> res = new HttpResponse<>();
            res.setStatus(400);
            res.setMessage("Mật khẩu xác nhận không khớp");
            registerResponse.setValue(res);
            return;
        }
        try {
            if (validateData()) {
//               //call api login here
                RegisterStatus.setValue(Status.registerSuccess);
            }
        } catch (Exception ex) {
            Log.e("Register viewmodel",ex.getMessage());
        }

        isLoading.setValue(true);
        Call<HttpResponse<RegisterResponseDTO>> repos = Retrofit.getInstance().Request().Register(user);

        repos.enqueue(new Callback<HttpResponse<RegisterResponseDTO>>() {
            @Override
            public void onResponse(Call<HttpResponse<RegisterResponseDTO>> call, Response<HttpResponse<RegisterResponseDTO>> response) {
                if (!response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        HttpResponse<RegisterResponseDTO> res = gson.fromJson(response.errorBody().charStream(), HttpResponse.class);
                        isLoading.setValue(false);
                        registerResponse.setValue(res);
                    } catch (Exception e) {
                        HttpResponse<RegisterResponseDTO> res = new HttpResponse<>();
                        res.setStatus(400);
                        res.setMessage("Đăng ký thất bại, Lỗi: " + e.getMessage());
                        isLoading.setValue(false);
                        registerResponse.setValue(res);
                    }

                    return;
                }

                HttpResponse<RegisterResponseDTO> post = response.body();
                isLoading.setValue(false);
                registerResponse.setValue(post);
            }

            @Override
            public void onFailure(Call<HttpResponse<RegisterResponseDTO>> call, Throwable t) {
                // Xử lý khi request thất bại
                Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                HttpResponse<RegisterResponseDTO> res = new HttpResponse<>();
                res.setStatus(400);
                res.setMessage("Kết nối đến máy chủ thất bại.");
                isLoading.setValue(false);
                registerResponse.setValue(res);
            }
        });
    }
    private boolean validateData() {
        try {
            if (ValidatorUtils.emptyValue(user.getEmail())) {
                RegisterStatus.setValue(Status.emptyEmail);
                return false;
            } else if (ValidatorUtils.emptyValue(user.getUsername())) {
                RegisterStatus.setValue(Status.emptyUsername);
                return false;
            } else if (ValidatorUtils.emptyValue(user.getPassword())) {
                RegisterStatus.setValue(Status.emptyPassWord);
                return false;
            } else if (ValidatorUtils.emptyValue(confirmPassword)) {
                RegisterStatus.setValue(Status.emptycfPassword);
                return false;
            } else if (!ValidatorUtils.isEmail(user.getEmail())) {
                RegisterStatus.setValue(Status.isEmail);
                return false;
            }
        } catch (Exception ex) {
            Log.e("Validate viewmodel",ex.getMessage());
        }
        return true;
    }
}
