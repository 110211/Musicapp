package com.huce.hma.presentation.splash;

import static com.huce.hma.common.utils.Utils.writeResponseBodyToDisk;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.huce.hma.BuildConfig;
import com.huce.hma.R;
import com.huce.hma.common.dto.AppVersionDTO;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.data.remote.services.Retrofit;
import com.huce.hma.presentation.app.home.HomeActivity;
import com.huce.hma.presentation.guest.login.LoginActivity;
import com.huce.hma.presentation.guest.login.dto.LoginResponseDTO;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashViewModel extends AndroidViewModel {
    public MutableLiveData<HttpResponse<AppVersionDTO>> checkUpdateResp = new MutableLiveData<>();
    public MutableLiveData<String> downloadUpdatePath= new MutableLiveData<>();

    public MutableLiveData<HttpResponse<LoginResponseDTO>> loginResp = new MutableLiveData<>();

    public SplashViewModel(@NonNull Application application) {
        super(application);
    }

    public void checkUpdate() {
        HttpResponse<AppVersionDTO> resp = new HttpResponse<>();
        Call<HttpResponse<AppVersionDTO>> repos = Retrofit.getInstance().Request().GetLatestVersion();

        repos.enqueue(new Callback<HttpResponse<AppVersionDTO>>() {
            @Override
            public void onResponse(Call<HttpResponse<AppVersionDTO>> call, Response<HttpResponse<AppVersionDTO>> response) {
                if (!response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        HttpResponse<AppVersionDTO> respObj = gson.fromJson(response.errorBody().charStream(), HttpResponse.class);
                        checkUpdateResp.setValue(respObj);
                    } catch (Exception e) {
                        resp.setMessage("Kiểm tra cập nhật thất bại. Lỗi: " + e.getMessage());
                        checkUpdateResp.setValue(resp);
                    }
                    return;
                }


                checkUpdateResp.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HttpResponse<AppVersionDTO>> call, Throwable t) {
                resp.setMessage("Kết nối đến máy chủ thất bại.");
                checkUpdateResp.setValue(resp);
            }
        });
    }

    public void downloadUpdate(Long id, String path) {
        Call<ResponseBody> repos = Retrofit.getInstance().Request().downloadAPKFile(
                BuildConfig.API_URL + "/api/v1/apk/" + id.toString() + "/download"
        );

        repos.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d("Updater", "server contacted and has file");
                    boolean writtenToDisk = writeResponseBodyToDisk(response.body(), path);

                    if (writtenToDisk) {
                        downloadUpdatePath.setValue(path);
                    }
                    Log.d("Updater", "file download was a success? " + writtenToDisk);
                } else {
                    Log.d("Updater", "server contact failed");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("Updater", "error");
            }
        });
    }

    public void getInfo() {

        HttpResponse<LoginResponseDTO> resp = new HttpResponse<>();
        Call<HttpResponse<LoginResponseDTO>> repos = Retrofit.getInstance().RequestAuth(getApplication().getApplicationContext())
                .getInfo();

        repos.enqueue(new Callback<HttpResponse<LoginResponseDTO>>() {
            @Override
            public void onResponse(Call<HttpResponse<LoginResponseDTO>> call, Response<HttpResponse<LoginResponseDTO>> response) {
                if (!response.isSuccessful()) {
                    try {
                        Gson gson = new Gson();
                        HttpResponse<LoginResponseDTO> respObj = gson.fromJson(response.errorBody().charStream(), HttpResponse.class);
                        loginResp.setValue(respObj);
                    } catch (Exception e) {
                        resp.setMessage("Check thông tin thất bại. Lỗi: " + e.getMessage());
                        loginResp.setValue(resp);
                    }
                    return;
                }
                loginResp.setValue(response.body());
            }

            @Override
            public void onFailure(Call<HttpResponse<LoginResponseDTO>> call, Throwable t) {
                resp.setMessage("Kết nối đến máy chủ thất bại.");
                loginResp.setValue(resp);
            }
        });

    }
}
