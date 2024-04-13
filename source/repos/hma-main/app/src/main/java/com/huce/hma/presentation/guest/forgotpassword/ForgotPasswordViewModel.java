package com.huce.hma.presentation.guest.forgotpassword;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.huce.hma.common.validator.ValidatorService;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.presentation.guest.forgotpassword.dto.ForgotPasswordRequestDTO;
import com.huce.hma.presentation.guest.forgotpassword.dto.ForgotPasswordResponseDTO;
import com.huce.hma.data.remote.services.Retrofit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordViewModel extends AndroidViewModel {
    public ForgotPasswordRequestDTO user = new ForgotPasswordRequestDTO();
    public MutableLiveData<HttpResponse<ForgotPasswordResponseDTO>> forgotResponse = new MutableLiveData<>();
    public MutableLiveData<Boolean> isLoading = new MutableLiveData<>();

    public MutableLiveData<Boolean> registerClick = new MutableLiveData<>();

    public ForgotPasswordViewModel(@NonNull Application application) {
        super(application);
    }

    public void forgotPasswordHandler() {
        Map<String, String> rules = new HashMap<>();
        rules.put("email", "required|email");

        ArrayList<String> errors = ValidatorService.validate(user, rules);

        if (!errors.isEmpty()) {
            HttpResponse<ForgotPasswordResponseDTO> res = new HttpResponse<>();
            res.setMessage(errors.get(0));
            forgotResponse.setValue(res);
        } else {
            isLoading.setValue(true);
            Call<HttpResponse<ForgotPasswordResponseDTO>> repos = Retrofit.getInstance().Request().ForgotPassword(user);

            repos.enqueue(new Callback<HttpResponse<ForgotPasswordResponseDTO>>() {
                @Override
                public void onResponse(Call<HttpResponse<ForgotPasswordResponseDTO>> call, Response<HttpResponse<ForgotPasswordResponseDTO>> response) {
                    if (!response.isSuccessful()) {
                        try {
                            Gson gson = new Gson();
                            isLoading.setValue(false);
                            HttpResponse<ForgotPasswordResponseDTO> res = gson.fromJson(response.errorBody().charStream(), HttpResponse.class);
                            forgotResponse.setValue(res);
                        } catch (Exception e) {
                            HttpResponse<ForgotPasswordResponseDTO> res = new HttpResponse<>();
                            res.setStatus(400);
                            res.setMessage("Yêu cầu mật khẩu thất bại, Lỗi:" + e.getMessage());
                            isLoading.setValue(false);
                            forgotResponse.setValue(res);
                        }
                        return;
                    }

                    HttpResponse<ForgotPasswordResponseDTO> post = response.body();
                    isLoading.setValue(false);
                    forgotResponse.setValue(post);
                }


                @Override
                public void onFailure(Call<HttpResponse<ForgotPasswordResponseDTO>> call, Throwable t) {
                    // Xử lý khi request thất bại
                    Log.d("Error", "Thất bại!. Lỗi: " + t.getMessage());
                    HttpResponse<ForgotPasswordResponseDTO> res = new HttpResponse<>();
                    res.setStatus(400);
                    res.setMessage("Kết nối đến máy chủ thất bại.");
                    isLoading.setValue(false);
                    forgotResponse.setValue(res);
                }
            });
        }
    }

    public void registerHandler() {
        registerClick.setValue(true);
    }
}
