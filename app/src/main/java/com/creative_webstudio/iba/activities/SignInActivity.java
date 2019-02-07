package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.widget.Button;
import android.widget.EditText;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.components.CustomRetryDialog;
import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.ConfigurationVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.mvp.presenters.SignInPresenter;
import com.creative_webstudio.iba.mvp.views.SignInView;
import com.creative_webstudio.iba.utils.CustomDialog;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;


import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity implements SignInView {
    @BindView(R.id.btnSignIn)
    Button btnSignIn;

    @BindView(R.id.etUserName)
    EditText etUserName;

    @BindView(R.id.etPassword)
    EditText etPassword;

    private SignInPresenter mPresenter;
    private boolean connected = false;

    //RetryDialog
    CustomRetryDialog dialog;

    //show loading
    AlertDialog loadingDialog;

    ConfigurationVO configurationVO;

    private IBAPreferenceManager ibaShared;

    public static Intent newIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this, this);

        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading.Please wait!");
        mPresenter = ViewModelProviders.of(this).get(SignInPresenter.class);
        mPresenter.initPresenter(this);
        ibaShared = new IBAPreferenceManager(this);
//        getConfigurationData();
        dialog =   new CustomRetryDialog(SignInActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        btnSignIn.setOnClickListener(view -> {
//            Crashlytics.log(Log.ERROR,SignInActivity.class.getSimpleName(),"ForceCrash");
//            Crashlytics.getInstance().crash();
            signIn();
        });
    }

    private void getConfigurationData() {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getConfigurationData();
            });
        }else {
            IbaModel.getInstance().getConfigData().observe(this, apiResponse -> {
                if (apiResponse.getData() != null) {
                    configurationVO = apiResponse.getData();
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                        } else {
                            retryDialog.show();
                            retryDialog.tvRetry.setText("No Internet Connection");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                getConfigurationData();
                            });
                        }
                    }
                }
            });
        }
    }

    public void signIn() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else {
            connected = false;
        }

        if (!connected) {
            dialog.show();
            dialog.tvRetry.setText("No Internet Connection");
            dialog.btnRetry.setOnClickListener(v -> {
                dialog.dismiss();
                signIn();
            });
            return;
        }

        if (etUserName.getText().toString().trim().equalsIgnoreCase("")) {
            etUserName.setError("Enter UserName");
        } else if (etPassword.getText().toString().trim().equalsIgnoreCase("")) {
            etPassword.setError("Enter Password");
        } else {
            btnSignIn.setClickable(false);
            mPresenter.getToken(etUserName.getText().toString(), etPassword.getText().toString());
            getResponse();
        }

    }

    public void getResponse(){
        loadingDialog.show();
        mPresenter.getResponseCode().observe(this, integer -> {
            if(loadingDialog.isShowing()){
                loadingDialog.dismiss();
            }
            switch (integer) {
                case 200:
                    startActivity(SplashActivity.newIntent(SignInActivity.this));
                    break;
                case 400:
                    dialog.show();
                    dialog.tvRetry.setText("Invalid User Name or Password");
                    dialog.btnRetry.setOnClickListener(v -> {
//                                tryConnection();
                        dialog.dismiss();
                        btnSignIn.setClickable(true);
                    });
                    break;
                default:
                    dialog.show();
                    dialog.tvRetry.setText("Network Error");
                    dialog.btnRetry.setOnClickListener(v -> {
                        signIn();
                        dialog.dismiss();
                    });
                    Snackbar.make(btnSignIn, "Network Error", Snackbar.LENGTH_LONG).show();
                    break;
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(!loadingDialog.isShowing()){
            moveTaskToBack(true);
            super.onBackPressed();
        }
    }
}
