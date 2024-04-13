package com.huce.hma.data.remote.repository;


import com.huce.hma.common.dto.AppVersionDTO;
import com.huce.hma.data.common.dto.User;
import com.huce.hma.presentation.app.home.DTO.ProfileDTO;
import com.huce.hma.presentation.app.home.DTO.SongSearchResultDTO;
import com.huce.hma.data.remote.dto.HttpResponse;
import com.huce.hma.presentation.app.profile.changepassword.dto.ChangePasswordRequestDto;
import com.huce.hma.presentation.guest.forgotpassword.dto.ForgotPasswordRequestDTO;
import com.huce.hma.presentation.guest.forgotpassword.dto.ForgotPasswordResponseDTO;
import com.huce.hma.presentation.guest.login.dto.LoginGoogleRequestDTO;
import com.huce.hma.presentation.guest.login.dto.LoginRequestDTO;
import com.huce.hma.presentation.guest.login.dto.LoginResponseDTO;
import com.huce.hma.presentation.guest.register.dto.RegisterRequestDTO;
import com.huce.hma.presentation.guest.register.dto.RegisterResponseDTO;

import java.util.ArrayList;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface APIRepository {
        /// auth
        @POST("/api/v1/register")
        Call<HttpResponse<RegisterResponseDTO>> Register(@Body RegisterRequestDTO registerRequestDTO);
        @POST("/api/v1/reset-password")
        Call<HttpResponse<ForgotPasswordResponseDTO>> ForgotPassword(@Body ForgotPasswordRequestDTO forgotPasswordRequestDTO);
        @POST("/api/v1/login")
        Call<HttpResponse<LoginResponseDTO>> Login(@Body LoginRequestDTO loginRequestDTO);
        @POST("/api/v1/login-google")
        Call<HttpResponse<LoginResponseDTO>> LoginGoogle(@Body LoginGoogleRequestDTO loginRequestDTO);

        /// update
        @GET("/api/v1/apk/latest")
        Call<HttpResponse<AppVersionDTO>> GetLatestVersion();

        @GET
        Call<ResponseBody> downloadAPKFile(@Url String fileUrl);

        /// user profile
        @GET("/api/v1/me")
        Call<HttpResponse<ProfileDTO>> getProfile();
        @GET("/api/v1/me")
        Call<HttpResponse<LoginResponseDTO>> getInfo();
        @GET("/api/v1/get-otp")
        Call<HttpResponse<String>> GetOTP();
        @POST("/api/v1/change-password")
        Call<HttpResponse<String>> ChangePassword(@Body ChangePasswordRequestDto changePasswordRequestDto);

        // app
        @GET("/api/v1/songs")
        Call<HttpResponse<ArrayList<SongSearchResultDTO>>> SearchSongsByKeyword(@Query("key") String keyword);
        @GET("/api/v1/songs/trending")
        Call<HttpResponse<ArrayList<SongSearchResultDTO>>> getTrendingMusicFile();

        @POST("/api/v1/song/{id}/like")
        Call<HttpResponse<Object>> setLikeSong(@Path("id") Long id);

}
