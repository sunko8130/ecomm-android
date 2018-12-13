package com.creative_webstudio.iba.activities;

import android.support.v7.app.AppCompatActivity;
import android.util.Base64;

import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.AppConstants;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public abstract class BaseActivity extends AppCompatActivity {

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

    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {

    }
    public void onAccessTokenRefreshFailure(Throwable t) {

    }

}
