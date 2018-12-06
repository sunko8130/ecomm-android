package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.mvp.presenters.SignInPresenter;
import com.creative_webstudio.iba.mvp.views.SignInView;
import com.creative_webstudio.iba.utils.AppConstants;
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

    private IBAPreferenceManager ibaShared;
    public static Intent newIntent(Context context) {
        return new Intent(context, SignInActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this, this);

        mPresenter = ViewModelProviders.of(this).get(SignInPresenter.class);
        mPresenter.initPresenter(this);
        ibaShared = new IBAPreferenceManager(this);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                        connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                    //we are connected to a network
                    connected = true;
                }
                else
                    connected = false;
                if (etUserName.getText().toString().trim().equalsIgnoreCase("")) {
                    etUserName.setError("Enter UserName");
                }else if(etPassword.getText().toString().trim().equalsIgnoreCase("")){
                    etPassword.setError("Enter Password");
                }else if(!connected){
                    Snackbar.make(btnSignIn, "No Internet Connection", Snackbar.LENGTH_LONG).show();
                }else {
                    mPresenter.getToken(etUserName.getText().toString(), etPassword.getText().toString());
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.getResponseCode().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                switch (integer) {
                    case 200:
                        startActivity(ProductActivity.newIntent(SignInActivity.this));
                        break;
                    case 400:
                        Snackbar.make(btnSignIn, "Invalid User Name or Password", Snackbar.LENGTH_LONG).show();
                        break;
                    default:
                        Snackbar.make(btnSignIn, "Network Error", Snackbar.LENGTH_LONG).show();
                        break;
                }
            }
        });
        Log.e("Shared", "onResume: "+ibaShared.fromPreference("AccessToken",""));
        Log.e("Shared", "onResume: "+ibaShared.fromPreference("RefreshToken",""));
    }
}
