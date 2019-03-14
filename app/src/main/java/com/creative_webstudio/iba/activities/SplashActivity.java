package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;
import android.widget.ImageView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.CustomerVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.mvp.views.BaseView;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by DELL on 11/19/2018.
 */

public class SplashActivity extends BaseActivity implements BaseView {
    @Nullable
    @BindView(R.id.ivSplash)
    ImageView ivSplash;

    private IBAPreferenceManager ibaShared;
    private String refreshToken = "";


    public static Intent newIntent(Context context) {
        return new Intent(context, SplashActivity.class);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this, this);
        ibaShared = new IBAPreferenceManager(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        refreshToken = ibaShared.fromPreference("RefreshToken", null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (refreshToken == null) {
            startActivity(SignInActivity.newIntent(SplashActivity.this));
        } else {
            getCustomerInfo();
        }
    }

    private void getCustomerInfo() {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getCustomerInfo();
            });
        } else {
            IbaModel.getInstance().getCustomer().observe(this, apiResponse -> {
                if (apiResponse.getData() != null) {
                    CustomerVO customerVO = IbaModel.getInstance().getCustomerVO();
                    mFirebaseAnalytics.setUserProperty("customer_name", customerVO.getName());
                    startActivity(MainActivity.newIntent(SplashActivity.this));
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            // Invalid or expired access token.
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // Because of SuperUser
                            retryDialog.show();
                            retryDialog.tvRetry.setText("You can't sign in with this account!");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                startActivity(SignInActivity.newIntent(this));
                            });
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                        } else {
                            retryDialog.show();
                            retryDialog.tvRetry.setText("Connection Error!");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                getCustomerInfo();
                            });
                        }
                    }
                }
            });
        }

    }


}
