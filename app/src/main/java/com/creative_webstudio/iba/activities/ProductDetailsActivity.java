package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.vos.HCInfoVO;
import com.creative_webstudio.iba.mvp.presenters.ProductDetailsPresenter;
import com.creative_webstudio.iba.mvp.views.ProductDetailView;

import org.mmtextview.components.MMTextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductDetailsActivity extends AppCompatActivity implements ProductDetailView {
    @BindView(R.id.tv_toolbar_title)
    MMTextView tvToolBarTitle;

    @BindView(R.id.iv_middle)
    ImageView ivMiddle;

    @Nullable
    @BindView(R.id.iv_back_toolbar)
    ImageView ivBackToolBar;

    @BindView(R.id.iv_back_rl)
    ImageView ivBack;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    String backActivity = "Product";

    @BindView(R.id.tv_item_name)
    MMTextView tvItemName;

    @BindView(R.id.iv_details_top_image)
    ImageView ivDetailTopImage;

    @BindView(R.id.tv_item_content)
    MMTextView tvItemContent;

    private ProductDetailsPresenter mPresenter;

    public static Intent newIntent(Context context, String backActivity) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra("BackActivity", backActivity);
        return intent;
    }

    public static Intent newIntent(Context context, String backActivity, double infoVO) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra("BackActivity", backActivity);
        intent.putExtra("infoVo", infoVO);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_details);
        ButterKnife.bind(this, this);
        Toolbar toolbar = findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (getIntent().hasExtra("BackActivity")) {
            backActivity = getIntent().getStringExtra("BackActivity");
        }


        mPresenter = ViewModelProviders.of(this).get(ProductDetailsPresenter.class);
        mPresenter.initPresenter(this);
        if (getIntent().hasExtra("infoVo")) {
            double infoId = getIntent().getDoubleExtra("infoVo",0);
            mPresenter.getInfoById(infoId);
        }

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

//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                        getWindow().setFlags(WindowManager.LayoutParams.Flag,
//                                WindowManager.LayoutParams.FLAGS_CHANGED);
//                    }
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
                onBackPressed();
                break;
        }
        return true;
    }

//    public void nextActivity() {
//        if(backActivity.equals("Product")) {
//            startActivity(ProductActivity.newIntent(this));
//        }else if(backActivity.equals("Cart")){
//            super.onBackPressed();
//        }else if(backActivity.equals("Search")){
//            super.onBackPressed();
//        }
////        overridePendingTransition(R.anim.zoom_in_anim, R.anim.rotate_anticlockwise_anim);
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void showInfoById(HCInfoVO hcInfoVO) {
        //Sample
        Glide.with(this)
                .load(hcInfoVO.getImage())
                .into(ivDetailTopImage);
        Glide.with(this)
                .load(hcInfoVO.getImage())
                .into(ivMiddle);
        tvItemName.setText(hcInfoVO.getTitle());
        tvItemContent.setText(hcInfoVO.getShortDec());
    }
}
