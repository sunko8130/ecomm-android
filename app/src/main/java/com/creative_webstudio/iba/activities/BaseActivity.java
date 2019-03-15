package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.creative_webstudio.iba.components.CustomRetryDialog;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.AppConstants;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.lang.reflect.Method;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public abstract class BaseActivity extends AppCompatActivity {
    //RetryDialog
    CustomRetryDialog retryDialog;

    //FirebaseAnalytics
    FirebaseAnalytics mFirebaseAnalytics;

    public boolean isTokenCalling = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retryDialog = new CustomRetryDialog(this);
        retryDialog.setCanceledOnTouchOutside(false);
        retryDialog.setCancelable(false);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }

    protected void refreshAccessToken() {
        IBAPreferenceManager prefs = new IBAPreferenceManager(this);
        String refreshToken = prefs.fromPreference("RefreshToken", "");
        String base = AppConstants.CLIENT_ID + ":" + AppConstants.CLIENT_SECRET;
        String userAuth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        IbaAPI api = ServiceGenerator.createSignInService(IbaAPI.class);
        api.getTokenByRefresh(userAuth, refreshToken, "refresh_token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(authResponse -> {
                    if (authResponse.isSuccessful()) {
                        onAccessTokenRefreshSuccess(authResponse);

                    } else {
                        onAccessTokenRefreshFailure(new ApiException(authResponse.code()));
                    }
                }, throwable -> {
                    // Network related error
                    onAccessTokenRefreshFailure(throwable);
                });
    }

    public boolean checkNetwork() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {
        TokenVO tokenVO = response.body();
        IBAPreferenceManager prefs = new IBAPreferenceManager(this);
        prefs.toPreference("AccessToken", tokenVO.getAccessToken());
        prefs.toPreference("RefreshToken", tokenVO.getRefreshToken());
        recreate();
    }

    public void onAccessTokenRefreshFailure(Throwable t) {
        if (t instanceof ApiException) {
            // Server response with one of the HTTP error status code.
            int errorCode = ((ApiException) t).getErrorCode();
            if (errorCode == 400 || errorCode == 401) {
                // Refresh token is expired.
                startActivity(new Intent(this, SignInActivity.class));
            } else {
                // TODO: Show the retry button
                retryDialog.show();
                retryDialog.tvRetry.setText("Network Error");
                retryDialog.btnRetry.setOnClickListener(v -> {
                    retryDialog.dismiss();
                    refreshAccessToken();
                });
            }
        } else {
            // TODO: Show the retry button for network related error.
            retryDialog.show();
            retryDialog.tvRetry.setText("Network related Error");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                refreshAccessToken();
            });
        }
    }

}
