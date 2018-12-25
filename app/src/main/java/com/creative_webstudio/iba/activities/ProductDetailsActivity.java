package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.components.CountDrawable;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.OrderUnitVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.SpinnerVO;
import com.creative_webstudio.iba.mvp.presenters.ProductDetailsPresenter;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.creative_webstudio.iba.utils.LoadImage;
import com.google.gson.Gson;

import org.mmtextview.components.MMTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ProductDetailsActivity extends BaseActivity{
    @BindView(R.id.tv_toolbar_title)
    MMTextView tvToolBarTitle;

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

    @BindView(R.id.btn_addToCart)
    Button btnAddToCart;

    //Toolbar
    @BindView(R.id.toolbar_details)
    Toolbar toolbar;

    private ProductDetailsPresenter mPresenter;

    private ProductVO productVO;

    private CartVO cartVO;

    private IBAPreferenceManager ibaShared;

    private int quantity = 1;

    private int minQuantity = 1;

    private int selectedItem = 0;

    private List<OrderUnitVO> orderUnitVOList;

    //cart items
    private int cartItems = 0;

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
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ibaShared = new IBAPreferenceManager(this);
        if (getIntent().hasExtra("BackActivity")) {
            backActivity = getIntent().getStringExtra("BackActivity");
        }
        orderUnitVOList = new ArrayList<>();

//        mPresenter = ViewModelProviders.of(this).get(ProductDetailsPresenter.class);
//        mPresenter.initPresenter(this);

        tvQuantity.setText(String.valueOf(quantity));
        if (getIntent().hasExtra("productVo")) {
            String json = getIntent().getStringExtra("productVo");
            Gson gson = new Gson();
            productVO = gson.fromJson(json, ProductVO.class);
            tvToolBarTitle.setText(productVO.getProductName());
            tvItemName.setText(productVO.getProductName());
            if (productVO.getDescription() != null) {
                tvItemContent.setText(productVO.getDescription());
            }
            orderUnitVOList = productVO.getOrderUnits();
            tvPrice.setText(String.valueOf(orderUnitVOList.get(selectedItem).getPricePerUnit()) + " MMK");
            if(!productVO.getThumbnailIdsList().isEmpty()){
                GlideUrl glideUrl = LoadImage.getGlideUrl(ibaShared.getAccessToken(),productVO.getThumbnailIdsList().get(0));
                Glide.with(this).asBitmap().apply(LoadImage.getOption()).load(glideUrl).into(ivDetailTopImage);
            }
            setUpSpinner();
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
                    builder.setTitle("Sure?");
                    builder.setMessage("Want to add to Cart?");
                    builder.setPositiveButton("Ok", (dialog, which) -> {
                        addToCart();
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    AlertDialog productDialog = builder.create();
                    productDialog.show();
                }
            });
        }
    }

    private void addToCart() {
        cartVO = new CartVO();
        cartVO.setProductId(productVO.getId());
        cartVO.setOrderUnitId(orderUnitVOList.get(selectedItem).getId());
        cartVO.setQuantity(quantity);
        ibaShared.addItemToCart(cartVO);
        onResume();
    }


    private void setUpSpinner() {
        ArrayList<SpinnerVO> unitList = new ArrayList<>();

        for (OrderUnitVO order : orderUnitVOList) {
            unitList.add(new SpinnerVO(order.getUnitName(), order.getItemsPerUnit(), order.getItemName()));
        }
        ArrayAdapter<SpinnerVO> orderAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, unitList);
        // Specify the layout to use when the list of choices appears
        orderAdapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spOrder.setAdapter(orderAdapter);

        spOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minQuantity = orderUnitVOList.get(i).getMinimumOrderQuantity();
                quantity = minQuantity;
                selectedItem = i;
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
            if (quantity > minQuantity) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            } else {
                Snackbar.make(tvPrice, "Minimum order is :" + minQuantity, Snackbar.LENGTH_SHORT).show();
            }


        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_cart:
                startActivity(CartActivity.newIntent(this));
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        setCount(this, cartItems);
        return true;
    }


    private void setCount(Context context, int count) {
        MenuItem menuItem = toolbar.getMenu().findItem(R.id.menu_cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();
        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse instanceof CountDrawable) {
            badge = (CountDrawable) reuse;
        } else {
            badge = new CountDrawable(context);
        }

        badge.setCount(String.valueOf(count));
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //load cart items
        if (ibaShared.getItemsFromCart() != null) {
            cartItems = 0;
            for (CartVO cartVO : ibaShared.getItemsFromCart()) {
                cartItems += cartVO.getQuantity();
            }

        } else {
            cartItems = 0;
        }
        invalidateOptionsMenu();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
}
