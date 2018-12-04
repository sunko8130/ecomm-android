package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.mvp.presenters.SplashPresenter;
import com.creative_webstudio.iba.mvp.views.SplashView;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by DELL on 11/19/2018.
 */

public class SplashActivity extends AppCompatActivity implements SplashView {

    @Nullable
    @BindView(R.id.ivSplash)
    ImageView ivSplash;

    private SplashPresenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this, this);

        mPresenter = ViewModelProviders.of(this).get(SplashPresenter.class);
        mPresenter.initPresenter(this);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ivSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });
    }

}
