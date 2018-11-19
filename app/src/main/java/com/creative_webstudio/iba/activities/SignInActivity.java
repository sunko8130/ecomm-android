package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.creative_webstudio.iba.BaseActivity;
import com.creative_webstudio.iba.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends BaseActivity {
    @BindView(R.id.btnSignIn)
    Button btnSignIn;

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,SignInActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this,this);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SignInActivity.this, ProductActivity.class);
                startActivity(i);
            }
        });
    }
}
