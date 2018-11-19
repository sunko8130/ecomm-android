package com.creative_webstudio.iba.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.creative_webstudio.iba.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by DELL on 11/19/2018.
 */

public class SplashActivity extends AppCompatActivity {

    @BindView(R.id.ivSplash)
    ImageView ivSplash;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this,this);

        ivSplash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SplashActivity.this, SignInActivity.class);
                startActivity(i);
            }
        });
    }

}
