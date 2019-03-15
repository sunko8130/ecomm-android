package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.model.GlideUrl;
import com.creative_webstudio.iba.MyOnPageChangeListener;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.DetailAdapter;
import com.creative_webstudio.iba.adapters.DetailBannerAdapter;
import com.creative_webstudio.iba.adapters.PromoAdapter;
import com.creative_webstudio.iba.adapters.SectionPagerAdapter;
import com.creative_webstudio.iba.components.CountDrawable;
import com.creative_webstudio.iba.components.TouchImageView;
import com.creative_webstudio.iba.components.ViewPagerFixed;
import com.creative_webstudio.iba.datas.criterias.PromoRewardDetailCriteria;
import com.creative_webstudio.iba.datas.vos.AdvertisementVO;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.OrderUnitVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.PromoRewardDetailVO;
import com.creative_webstudio.iba.datas.vos.PromoRewardVO;
import com.creative_webstudio.iba.datas.vos.SpinnerVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.ProductViewModel;
import com.creative_webstudio.iba.utils.CustomDialog;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.mmtextview.components.MMTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;


public class ProductDetailsActivity extends BaseActivity {
    @BindView(R.id.tv_toolbar_title)
    MMTextView tvToolBarTitle;

    @BindView(R.id.appbar)
    AppBarLayout appBarLayout;
    String backActivity = "Product";

    @BindView(R.id.tv_item_name)
    MMTextView tvItemName;

    @BindView(R.id.tv_item_content)
    MMTextView tvItemContent;

    @BindView(R.id.sp_order)
    Spinner spOrder;

    @BindView(R.id.iv_plus)
    ImageView ivPlus;

    @BindView(R.id.iv_minus)
    ImageView ivMinus;

    @BindView(R.id.ed_quantity)
    MMTextView tvQuantity;

    @BindView(R.id.tv_price)
    MMTextView tvPrice;

    @BindView(R.id.tv_in_stock)
    MMTextView tvInStock;

    @BindView(R.id.btn_addToCart)
    Button btnAddToCart;

    @BindView(R.id.rc_details)
    RecyclerView rcDetails;

    @BindView(R.id.rcPromo)
    RecyclerView rcPromo;

    //Toolbar
    @BindView(R.id.toolbar_details)
    Toolbar toolbar;

    //Image Scroll
    @Nullable
    @BindView(R.id.view_pager)
    AutoScrollViewPager viewPager;

    //photo view
    @BindView(R.id.rl_viewpager)
    RelativeLayout rlViewPager;

    @BindView(R.id.photo_view_pager)
    ViewPagerFixed photoViewPager;

    @BindView(R.id.layout_details)
    LinearLayout layoutDetails;

    private DetailAdapter mDetailAdapter;

    private PromoAdapter mPromoAdapter;

    private ProductVO productVO;

    private CartVO cartVO;

    private IBAPreferenceManager ibaShared;

    private int quantity = 1;

    private int minQuantity = 1;

    private int maxQuantity = 0;

    private int selectedItem = 0;

    private List<OrderUnitVO> orderUnitVOList;

    private List<PromoRewardVO> promoRewardVOList;

    private List<PromoRewardDetailVO> promoDetailList;

    private ProductViewModel mProductViewModel;

    //cart items
    private int cartItems = 0;

    //loading dialog
    AlertDialog loadingDialog;
    private CirclePageIndicator titlePageIndicator;

    //fro promo
    List<PromoRewardDetailCriteria> promoCriteriaList;

    List<Long> list;
    Context context;

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
        setContentView(R.layout.activity_product_details);
        ButterKnife.bind(this, this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        context = this;
        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Product Detail.Please wait!");
        mProductViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        mDetailAdapter = new DetailAdapter(this);
        rcDetails.setLayoutManager(new LinearLayoutManager(this));
        rcDetails.setAdapter(mDetailAdapter);
        mPromoAdapter = new PromoAdapter(this);
        rcPromo.setLayoutManager(new LinearLayoutManager(this));
        rcPromo.setAdapter(mPromoAdapter);
        promoRewardVOList = new ArrayList<>();

        ibaShared = new IBAPreferenceManager(this);
        if (getIntent().hasExtra("BackActivity")) {
            backActivity = getIntent().getStringExtra("BackActivity");
        }
        orderUnitVOList = new ArrayList<>();
        promoCriteriaList = new ArrayList<>();

        tvQuantity.setText(String.valueOf(quantity));
        if (getIntent().hasExtra("productVo")) {
            String json = getIntent().getStringExtra("productVo");
            Gson gson = new Gson();
            productVO = gson.fromJson(json, ProductVO.class);
            tvToolBarTitle.setText(productVO.getProductName());
            tvItemName.setText(productVO.getProductName());
            if (productVO.getDescription() != null) {
                String string = productVO.getDescription();
                string = string.replace("\n", "<br>");
                string = string.replace(" ", "&#160;");
                tvItemContent.setText(string);
//                tvItemContent.setText(string);
            }
            if (productVO.getOrderUnits() != null) {
                orderUnitVOList = productVO.getOrderUnits();
            }
            tvPrice.setText(String.valueOf(orderUnitVOList.get(selectedItem).getPricePerUnit()) + " MMK");
            list = new ArrayList<>();
            setupViewPager();
            if (productVO.getProductDetailsVo() != null && productVO.getProductDetailsVo().getValueList() != null) {
                layoutDetails.setVisibility(View.VISIBLE);
                mDetailAdapter.setNewData(productVO.getProductDetailsVo().getValueList());
            } else {
                layoutDetails.setVisibility(View.GONE);
            }
            if (productVO.getHasPromotion()) {
                for (OrderUnitVO orderUnitVO : productVO.getOrderUnits()) {
                    for (PromoRewardVO promoRewardVO : orderUnitVO.getPromoRewardVOList()) {
                        PromoRewardDetailCriteria promo = new PromoRewardDetailCriteria();
                        promo.setOrderUnitId(orderUnitVO.getId());
                        promo.setPromoRewardId(promoRewardVO.getId());
                        promoCriteriaList.add(promo);
                    }

                }
                getPromoDetail(promoCriteriaList);
            } else {
                setUpSpinner();
            }
            btnAddToCart.setOnClickListener(view -> {
                        if (maxQuantity < minQuantity) {
                            Toast.makeText(context, "You can't order for this item!", Toast.LENGTH_SHORT).show();
                        } else if (maxQuantity > 0) {
//                            final AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailsActivity.this);
//                            builder.setTitle("Sure?");
//                            builder.setMessage("Want to add to Cart?");
//                            builder.setPositiveButton("Ok", (dialog, which) -> {
//                                addToCart();
//                            });
//                            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
//                            AlertDialog productDialog = builder.create();
//                            productDialog.show();
                            List<CartVO> list = ibaShared.getItemsFromCart();
                            boolean isContain = false;
                            if (list == null || list.size() == 0) {
                                isContain = true;
                                addToCart();
                            } else {
                                for (CartVO cartVO : list) {
                                    if (cartVO.getProductId().equals(productVO.getId()) && cartVO.getOrderUnitId().equals(orderUnitVOList.get(selectedItem).getId())) {
                                        isContain = true;
                                        if (cartVO.getQuantity() + quantity <= maxQuantity) {
                                            addToCart();
                                        } else {
                                            Snackbar.make(rcPromo, "Your order is exceed the maximum", Snackbar.LENGTH_LONG).show();
                                        }
                                    }
                                }
                            }
                            if (!isContain) {
                                addToCart();
                            }

                        } else {
                            Toast.makeText(context, "This product is out of stock!", Toast.LENGTH_SHORT).show();
                        }
                    }
            );

        }
    }

    private void setupViewPager() {
        if (productVO.getThumbnailIdsList() == null || productVO.getThumbnailIdsList().isEmpty()) {
            list.add((long) 0);
        } else {
            list = productVO.getThumbnailIdsList();
        }
        DetailBannerAdapter adapter = new DetailBannerAdapter(this.getSupportFragmentManager(), list, 1);
        if (viewPager != null) {
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
            viewPager.setInterval(10000);
            viewPager.startAutoScroll();
            titlePageIndicator = findViewById(R.id.title_page_indicator);
            titlePageIndicator.setViewPager(viewPager);
            titlePageIndicator.setSnap(true);
        }
    }

    public void clickViewPager(Long imageId) {
        if (productVO.getThumbnailIdsList() == null || productVO.getThumbnailIdsList().isEmpty()) {
            Toast.makeText(context, "No photo to Display", Toast.LENGTH_SHORT).show();
        } else {
            btnAddToCart.setVisibility(View.GONE);
            rlViewPager.setVisibility(View.VISIBLE);
            int index = 0;
            for (int i = 0; i < productVO.getThumbnailIdsList().size(); i++) {
                if (imageId.equals(productVO.getThumbnailIdsList().get(i))) {
                    index = i;
                    break;
                }
            }
            DetailBannerAdapter adapter = new DetailBannerAdapter(this.getSupportFragmentManager(), list, 2);
            if (photoViewPager != null) {
                photoViewPager.setAdapter(adapter);
                photoViewPager.setCurrentItem(index);
            }
        }
    }

    private void getPromoDetail(List<PromoRewardDetailCriteria> criteria) {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getPromoDetail(criteria);
            });
        } else {
            loadingDialog.show();
            mProductViewModel.getPromoDetail(criteria).observe(this, apiResponse -> {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (apiResponse.getData() != null) {
                    promoDetailList = new ArrayList<>();
                    promoDetailList = apiResponse.getData();
                    setUpSpinner();
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(rcPromo, "End of Product List", Snackbar.LENGTH_LONG).show();
                        } else {
                            retryDialog.show();
                            retryDialog.tvRetry.setText("No Internet Connection");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                getPromoDetail(promoCriteriaList);
                            });
                        }
                    }
                }
            });
        }
    }

    private void addToCart() {
        Bundle bundle = new Bundle();
        bundle.putLong("product_id", productVO.getId());
        bundle.putString("product_name", productVO.getProductName());
        bundle.putString("unit_item", "1" + orderUnitVOList.get(selectedItem).getUnitName() + " per " + orderUnitVOList.get(selectedItem).getItemsPerUnit() + " " + orderUnitVOList.get(selectedItem).getItemName());
        bundle.putInt("quantity", quantity);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "add_to_cart");
        mFirebaseAnalytics.logEvent("click_add_to_cart", bundle);
        cartVO = new CartVO();
        cartVO.setProductId(productVO.getId());
        cartVO.setOrderUnitId(orderUnitVOList.get(selectedItem).getId());
        cartVO.setQuantity(quantity);
        cartVO.setUnitInStock(orderUnitVOList.get(selectedItem).getUnitInStock());
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
        setUpPromo();

        spOrder.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                minQuantity = orderUnitVOList.get(i).getMinimumOrderQuantity();
                maxQuantity = orderUnitVOList.get(i).getUnitInStock();
//                maxQuantity = 1000;
                quantity = minQuantity;
//                quantity=700;
                selectedItem = i;
                tvQuantity.setText(String.valueOf(quantity));
                String s = String.format("%,.2f", orderUnitVOList.get(i).getPricePerUnit());
                tvPrice.setText(s + " MMK");
                updatePromo(orderUnitVOList.get(i).getId());
                if (maxQuantity < 1) {
                    quantity = 0;
                    tvQuantity.setText(String.valueOf(quantity));
                    tvInStock.setText("Out of stock");
                    tvInStock.setTextColor(ContextCompat.getColor(ProductDetailsActivity.this, R.color.redFull));
                } else {
                    tvInStock.setText("In Stock");
                    tvInStock.setTextColor(ContextCompat.getColor(ProductDetailsActivity.this, R.color.limeGreen));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ivPlus.setOnClickListener(view -> {
            if (quantity >= maxQuantity) {
                Snackbar.make(tvPrice, "Available stock quantity is " + maxQuantity, Snackbar.LENGTH_SHORT).show();
            } else {
                quantity++;
                tvQuantity.setText(String.valueOf(quantity));
            }
            if (quantity == maxQuantity) {
                ivPlus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.redFull)));
            } else {
                ivMinus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blackFull)));
            }
            if (productVO.getHasPromotion()) {
                updatePromo(orderUnitVOList.get(selectedItem).getId());
            }
        });

        ivMinus.setOnClickListener(view -> {
            if (quantity > minQuantity) {
                quantity--;
                tvQuantity.setText(String.valueOf(quantity));
            } else {
                Snackbar.make(tvPrice, "Minimum order is :" + minQuantity, Snackbar.LENGTH_SHORT).show();
            }
            if (quantity == minQuantity) {
                ivMinus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.redFull)));
            } else {
                ivPlus.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.blackFull)));
            }
            if (productVO.getHasPromotion()) {
                updatePromo(orderUnitVOList.get(selectedItem).getId());
            }
        });
    }

    private void setUpPromo() {
        promoRewardVOList = new ArrayList<>();
        if (productVO.getHasPromotion()) {
            for (OrderUnitVO orderUnitVO : orderUnitVOList) {
                if (orderUnitVO.getPromoRewardVOList() != null) {
                    for (PromoRewardVO promoRewardVO : orderUnitVO.getPromoRewardVOList()) {
                        promoRewardVO.setQuantity(quantity);
                        promoRewardVO.setUnitId(orderUnitVO.getId());
                        promoRewardVO.setShowUnit("1" + orderUnitVO.getUnitName() + " per " + orderUnitVO.getItemsPerUnit() + " " + orderUnitVO.getItemName());
                        for (PromoRewardDetailVO detailVO : promoDetailList) {
                            if (detailVO.getOrderUnitId().equals(orderUnitVO.getId()) && detailVO.getPromoRewardId().equals(promoRewardVO.getId())) {
                                promoRewardVO.setPromoQuantity(detailVO.getOrderQuantity());
                            }
                        }
                        promoRewardVOList.add(promoRewardVO);
                    }
                }
            }
        }

        mPromoAdapter.setNewData(promoRewardVOList);
    }

    public void updatePromo(long promoId) {
        for (PromoRewardVO promoRewardVO : promoRewardVOList) {
            if (promoRewardVO.getUnitId().equals(promoId)) {
                promoRewardVO.setQuantity(quantity);
            } else {
                promoRewardVO.setQuantity(1);
            }
        }
        mPromoAdapter.setNewData(promoRewardVOList);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_cart:
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_cart");
                mFirebaseAnalytics.logEvent("click_cart", bundle);
                startActivity(CartActivity.newIntent(this));
                finish();
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
        if (btnAddToCart.getVisibility() == View.GONE) {
            rlViewPager.setVisibility(View.GONE);
            btnAddToCart.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }


}
