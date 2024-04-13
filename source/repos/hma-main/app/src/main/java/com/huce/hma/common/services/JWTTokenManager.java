package com.huce.hma.common.services;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.huce.hma.R;

public class JWTTokenManager {
    private static SharedPreferences sharedPreferences;

    public static void saveToken(Context ctx, String token) {
        sharedPreferences = ctx.getSharedPreferences(String.valueOf(R.string.app_preference_file_key), Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("jwtToken", token).apply();
    }

    public static String getToken(Context ctx) {
        sharedPreferences = ctx.getSharedPreferences(String.valueOf(R.string.app_preference_file_key), Context.MODE_PRIVATE);
        return sharedPreferences.getString("jwtToken", "");
    }
    public static void clearToken(Context ctx) {
        sharedPreferences = ctx.getSharedPreferences(String.valueOf(R.string.app_preference_file_key), Context.MODE_PRIVATE);
        sharedPreferences.edit().remove("jwtToken").apply();
    }

}
