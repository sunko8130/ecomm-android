package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.vos.HCInfoVO;
import com.creative_webstudio.iba.datas.vos.OrderUnitVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.SpinnerVO;
import com.creative_webstudio.iba.mvp.presenters.ProductDetailsPresenter;
import com.creative_webstudio.iba.mvp.views.ProductDetailView;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.google.gson.Gson;

import org.mmtextview.components.MMTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductDetailsActivity extends BaseActivity implements ProductDetailView {
    @BindView(R.id.tv_toolbar_title)
    MMTextView tvToolBarTitle;

//    @Nullable
//    @BindView(R.id.iv_back_toolbar)
//    ImageView ivBackToolBar;

//    @BindView(R.id.tv_product_name)
//    MMTextView tvProductName;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    String backActivity = "Product";

    @BindView(R.id.tv_item_name)
    MMTextView tvItemName;

    @BindView(R.id.iv_details_top_image)
    ImageView ivDetailTopImage;

    @BindView(R.id.tv_item_content)
    MMTextView tvItemContent;

    @BindView(R.id.sp_order)
    Spinner spOrder;

    @BindView(R.id.iv_plus)
    ImageView ivPlus;

    @BindView(R.id.iv_minus)
    ImageView ivMinus;

    @BindView(R.id.tv_quantity)
    MMTextView tvQuantity;

    @BindView(R.id.tv_price)
    MMTextView tvPrice;

    private ProductDetailsPresenter mPresenter;

    private ProductVO productVO;

    private IBAPreferenceManager ibaShared;

    private int quantity = 1;

    private int minQuantity= 1;

    private int selectedItem = 0;

    private List<OrderUnitVO> orderUnitVOList;

    public static Intent newIntent(Context context, String backActivity) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra("BackActivity", backActivity);
        return intent;
    }

    public static Intent newIntent(Context context, String backActivity, String productVo) {
        Intent intent = new Intent(context, ProductDetailsActivity.class);
        intent.putExtra("BackActivity", backActivity);
        intent.putExtra("productVo", productVo);
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
        ibaShared = new IBAPreferenceManager(this);
        if (getIntent().hasExtra("BackActivity")) {
            backActivity = getIntent().getStringExtra("BackActivity");
        }
        orderUnitVOList=new ArrayList<>();

        mPresenter = ViewModelProviders.of(this).get(ProductDetailsPresenter.class);
        mPresenter.initPresenter(this);

        tvQuantity.setText(String.valueOf(quantity));
        if (getIntent().hasExtra("productVo")) {
            String json = getIntent().getStringExtra("productVo");
            Gson gson = new Gson();
            productVO = gson.fromJson(json, ProductVO.class);
            tvToolBarTitle.setText(productVO.getProductName());
//            tvProductName.setText(productVO.getProductName());
            tvItemName.setText(productVO.getProductName());
            tvItemContent.setText(productVO.getDescription());
            orderUnitVOList = productVO.getOrderUnits();
            tvPrice.setText(String.valueOf(orderUnitVOList.get(selectedItem).getPricePerUnit())+" MMK");
            GlideUrl glideUrl = LoadImage.getGlideUrl(ibaShared.getAccessToken());
            Glide.with(this).asBitmap().apply(LoadImage.getOption()).load(glideUrl).into(ivDetailTopImage);
            setUpSpinner();
        }
    }

//    private void getOrderUnit(long productId) {
//        ProductDetailViewModel viewModel = ViewModelProviders.of(this).get(ProductDetailViewModel.class);
//        viewModel.getOrderUnit(productId).observe(this, apiResponse -> {
//            if (apiResponse.getData() != null) {
//                orderUnitVOList=apiResponse.getData().getOrderUnitVOS();
//
//            }else {
//                if (apiResponse.getError() instanceof ApiException) {
//                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
//                    if (errorCode == 401) {
//                        super.refreshAccessToken();
//                    } else if (errorCode == 204) {
//                        orderUnitVOList=new ArrayList<>();
//                    }
//                } else {
//                    // TODO: Network related error occurs. Show the retry button with status text so that user can retry.
//                }
//            }
//        });
//    }

    private void setUpSpinner() {
        ArrayList<SpinnerVO> unitList = new ArrayList<>();

        for(OrderUnitVO order:orderUnitVOList){
            unitList.add(new SpinnerVO(order.getUnitName(),order.getItemsPerUnit(),order.getItemName()));
        }
        ArrayAdapter<SpinnerVO> orderAdapter = new ArrayAdapter<>(this,R.layout.spinner_item,unitList);
        // Specify the layout to use when the list of choices appears
        orderAdapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spOrder.setAdapter(orderAdapter);

        spOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minQuantity = orderUnitVOList.get(i).getMinimumOrderQuantity();
                quantity=minQuantity;
                selectedItem=i;
                tvQuantity.setText(String.valueOf(quantity));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ivPlus.setOnClickListener(view -> {
            quantity++;
            tvQuantity.setText(String.valueOf(quantity));
        });

        ivMinus.setOnClickListener(view -> {
            if(quantity>minQuantity){
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            }else {
                Snackbar.make(tvPrice,"Minimum order is :"+minQuantity, Snackbar.LENGTH_SHORT).show();
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
        tvItemName.setText(hcInfoVO.getTitle());
        tvItemContent.setText(hcInfoVO.getShortDec());
    }
}
