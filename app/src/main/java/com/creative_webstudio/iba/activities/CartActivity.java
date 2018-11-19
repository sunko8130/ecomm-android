package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.creative_webstudio.iba.R;

/**
 * Created by DELL on 11/19/2018.
 */

public class CartActivity extends BaseDrawerActivity {

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,CartActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyView(R.layout.activity_cart);
    }
}
