package com.creative_webstudio.iba.activities;

import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.components.CustomRetryDialog;
import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.ConfigurationVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.mvp.presenters.SignInPresenter;
import com.creative_webstudio.iba.mvp.views.SignInView;
import com.creative_webstudio.iba.utils.AppConstants;
import com.creative_webstudio.iba.utils.CustomDialog;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;


import org.mmtextview.components.MMTextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity implements SignInView {
    @BindView(R.id.btnSignIn)
    Button btnSignIn;

    @BindView(R.id.etUserName)
    EditText etUserName;

    @BindView(R.id.etPassword)
    EditText etPassword;

    @BindView(R.id.tvContactMsg)
    MMTextView tvContactMsg;

    @BindView(R.id.tvPhone)
    MMTextView tvPhone;

    private SignInPresenter mPresenter;

    String phoneNumber;

    //RetryDialog
    CustomRetryDialog dialog;

    //show loading
    AlertDialog loadingDialog;

    List<ConfigurationVO> configurationVOList;

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
        dialog = new CustomRetryDialog(SignInActivity.this);
        dialog.setCanceledOnTouchOutside(false);
        btnSignIn.setOnClickListener(view -> {
            signIn();
        });
        getResponse();
        tvPhone.setOnClickListener(view -> {
            callPhoneNumber();
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 101)
        {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                callPhoneNumber();
            }
        }
    }

    public void callPhoneNumber()
    {
        try
        {
            if(Build.VERSION.SDK_INT > 22)
            {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(SignInActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);

            }
            else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + phoneNumber));
                startActivity(callIntent);
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
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
            IbaModel.getInstance().getConfigData("").observe(this, apiResponse -> {
                if (apiResponse.getData() != null) {
                    configurationVOList = apiResponse.getData();
                    setupConfig();
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

    private void setupConfig() {
        for(ConfigurationVO configurationVO:configurationVOList){
            if(configurationVO.getKey().equals(AppConstants.APP_CONTACT_MSG)){
                tvContactMsg.setText(configurationVO.getValue());
            }
            if(configurationVO.getKey().equals(AppConstants.APP_CONTACT_PHONE)){
                phoneNumber=configurationVO.getValue();
                tvPhone.setPaintFlags(tvContactMsg.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
                tvPhone.setText(configurationVO.getValue());
            }
        }
    }



    public void signIn() {
        if (!checkNetwork()) {
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
            loadingDialog.show();
            mPresenter.getToken(etUserName.getText().toString(), etPassword.getText().toString());
        }

    }

    public void getResponse(){
        mPresenter.getResponseCode().observe(this, integer -> {
            if(loadingDialog.isShowing()){
                loadingDialog.dismiss();
            }
            switch (integer) {
                case 200:
                    getConfigurationData();
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
