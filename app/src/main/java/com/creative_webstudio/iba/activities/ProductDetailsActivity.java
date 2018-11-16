package com.creative_webstudio.iba.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.creative_webstudio.iba.R;


public class ProductDetailsActivity extends AppCompatActivity {
    TextView tvToolBarTitle;
    ImageView ivMiddle;
    ImageView ivBackToolBar, ivBack;
    AppBarLayout appBarLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        appBarLayout = findViewById(R.id.appbar);
        tvToolBarTitle = findViewById(R.id.tv_toolbar_title);
        ivMiddle = findViewById(R.id.iv_middle);
        ivBackToolBar = findViewById(R.id.iv_back_toolbar);
        ivBack = findViewById(R.id.iv_back_rl);
//        ivBackToolBar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                nextActivity();
//            }
//        });


        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) == appBarLayout.getTotalScrollRange()) {
                    // If collapsed, then do this
                    tvToolBarTitle.setVisibility(View.VISIBLE);
                    ivMiddle.setVisibility(View.VISIBLE);

                } else if (verticalOffset == 0) {
                    // If expanded, then do this
                    tvToolBarTitle.setVisibility(View.GONE);
                    ivMiddle.setVisibility(View.GONE);


                }
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                nextActivity();
                break;
        }
        return true;
    }

    public void nextActivity() {
        Intent i = new Intent(this, ProductActivity.class);
        startActivity(i);
        overridePendingTransition(R.anim.zoom_in_anim, R.anim.rotate_anticlockwise_anim);
    }

    @Override
    public void onBackPressed() {
        nextActivity();
    }
}
