package com.creative_webstudio.iba.utils;

import android.content.Context;
import android.util.Base64;

import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ErrorHandler {
    Context context;

    public ErrorHandler(Context context) {
        this.context = context;
    }

    public boolean handle(ApiException ex) {
        if (ex == null) return false;

        int errorCode = ex.getErrorCode();
        if (errorCode == 401) {
            // Access token has expired and need to refresh the access token.
            IBAPreferenceManager prefs = new IBAPreferenceManager(context);
            String refreshToken = prefs.fromPreference("RefreshToken", "");
            String base = AppConstants.CLIENT_ID + ":" + AppConstants.CLIENT_SECRET;
            String userAuth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
            IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
            api.getTokenByRefresh(userAuth, refreshToken, "refresh_token")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(authResponse -> {
                        //
                    }, throwable -> {
                        // Go to sign in activity.
                    });
        }

        return false;
    }
}
