package com.creative_webstudio.iba;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.creative_webstudio.iba.activities.ProductActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity {
    @BindView(R.id.btnSignIn)
    Button btnSignIn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this, this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, ProductActivity.class);
                startActivity(i);
            }
        });
    }
}
