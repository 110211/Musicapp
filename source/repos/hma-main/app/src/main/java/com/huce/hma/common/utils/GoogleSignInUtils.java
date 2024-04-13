package com.huce.hma.common.utils;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

public class GoogleSignInUtils {
    public static GoogleSignInAccount getGoogleSignInAccountFromTask(Task<GoogleSignInAccount> completedTask) {
        try {
            return completedTask.getResult(ApiException.class);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("G Sign In", "signInResult:failed code=" + e.getStatusCode());
            return null;
        }
    }
}
