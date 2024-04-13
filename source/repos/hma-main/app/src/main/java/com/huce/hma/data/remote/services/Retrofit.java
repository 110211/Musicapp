package com.huce.hma.data.remote.services;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.huce.hma.BuildConfig;
import com.huce.hma.R;
import com.huce.hma.common.services.JWTTokenManager;
import com.huce.hma.data.remote.repository.APIRepository;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit {
    public static Retrofit retrofit;

    public Retrofit(){

    }

    public APIRepository RequestAuth(Context ctx) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client =  new OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addInterceptor(chain -> {
                Request newRequest  = chain.request().newBuilder()
                        .addHeader("Authorization", "Bearer " + JWTTokenManager.getToken(ctx))
                        .build();
                return chain.proceed(newRequest);
            })
            .build();

        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
        .baseUrl(BuildConfig.API_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build();
        return retrofit.create(APIRepository.class);
    }

    public APIRepository Request() {
        retrofit2.Retrofit retrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(BuildConfig.API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(APIRepository.class);
    }

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit();
        }
        return retrofit;
    }
}
