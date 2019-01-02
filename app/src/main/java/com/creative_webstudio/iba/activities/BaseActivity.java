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

import java.lang.reflect.Method;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public abstract class BaseActivity extends AppCompatActivity {
    //RetryDialog
    CustomRetryDialog retryDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retryDialog = new CustomRetryDialog(this);
        retryDialog.setCanceledOnTouchOutside(false);
    }

    protected void refreshAccessToken() {
        IBAPreferenceManager prefs = new IBAPreferenceManager(this);
        String refreshToken = prefs.fromPreference("RefreshToken", "");
        String base = AppConstants.CLIENT_ID + ":" + AppConstants.CLIENT_SECRET;
        String userAuth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
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

    public boolean checkNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        } else {
            return false;
        }
    }

    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {

    }
    public void onAccessTokenRefreshFailure(Throwable t) {
        if (t instanceof ApiException) {
            // Server response with one of the HTTP error status code.
            int errorCode = ((ApiException) t).getErrorCode();
            if (errorCode == 401) {
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
