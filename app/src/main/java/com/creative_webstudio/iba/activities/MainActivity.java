package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.GlideException;
import com.creative_webstudio.iba.MyOnPageChangeListener;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.CategoryAdapter;
import com.creative_webstudio.iba.adapters.SectionPagerAdapter;
import com.creative_webstudio.iba.components.CountDrawable;
import com.creative_webstudio.iba.components.CustomRetryDialog;
import com.creative_webstudio.iba.components.EmptyViewPod;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.datas.vos.AdvertisementVO;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.ProductViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

public class MainActivity extends BaseDrawerActivity {

    @Nullable
    @BindView(R.id.rv_category)
    SmartRecyclerView rvCategory;

    @Nullable
    @BindView(R.id.view_pager)
    AutoScrollViewPager viewPager;

    @Nullable
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @BindView(R.id.vp_empty_product)
    EmptyViewPod vpEmpty;

    @Nullable
    @BindView(R.id.btn_refresh_empty)
    TextView btnEmpty;

    @Nullable
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    @Nullable
    @BindView(R.id.rl_viewpager)
    RelativeLayout rlViewPager;

    @Nullable
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScrollView;

    @Nullable
    @BindView(R.id.header)
    TextView header;

    private CirclePageIndicator titlePageIndicator;

    private ArrayList<CategoryVO> mCategoryList;

    private ArrayList<AdvertisementVO> mAdvertisementList;

    private ProductViewModel mProductViewModel;

    private CategoryAdapter mCategoryAdapter;

    //Scroll top
    boolean scrollTop = true;

    GridLayoutManager layoutManager;

    private int cartItems = 0;

    public static Intent newIntent(Context context) {
        return new Intent(context, MainActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyView(R.layout.activity_main);
        ButterKnife.bind(this, this);

        rvCategory.setEmptyView(vpEmpty);
        mCategoryList = new ArrayList<>();
        mAdvertisementList = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(this);
        if (isTablet(this)) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 2);
        }
        rvCategory.setLayoutManager(layoutManager);
        rvCategory.setAdapter(mCategoryAdapter);
        mProductViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        tvEmpty.setText("Loading Data........");
        btnEmpty.setVisibility(View.GONE);
        getAdvertisement();

        swipeRefreshLayout.setOnRefreshListener(() -> {
            refreshData();
        });

        btnEmpty.setOnClickListener(v -> {
            refreshData();
        });

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == 0) {
                scrollTop = true;
            } else {
                scrollTop = false;
            }
        });
    }

    private void refreshData() {
        tvEmpty.setText("Loading Data......");
        btnEmpty.setVisibility(View.GONE);
        mCategoryAdapter.clearData();
        getCategory();
    }

    private void getAdvertisement() {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getAdvertisement();
            });
        } else {
            mProductViewModel.getAdvertisement().observe(this, apiResponse -> {
                if (apiResponse.getData() != null) {
                    mAdvertisementList = new ArrayList<>();
                    mAdvertisementList = (ArrayList<AdvertisementVO>) apiResponse.getData();
                    setupViewPager(mAdvertisementList);
                    getCategory();
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            rlViewPager.setVisibility(View.GONE);
                            getCategory();
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(rvCategory, "End of Product List", Snackbar.LENGTH_LONG).show();
                        } else {
                            retryDialog.show();
                            retryDialog.tvRetry.setText("No Internet Connection");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                getAdvertisement();
                            });
                        }
                    }
                }
            });
        }
    }

    private void getCategory() {
        if (!checkNetwork()) {
            if (swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(false);
            }
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getCategory();
            });
        } else {
            mProductViewModel.getCategory().observe(this, apiResponse -> {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (apiResponse.getData() != null) {
                    header.setVisibility(View.VISIBLE);
                    setupCategory();
                    mCategoryList.addAll(apiResponse.getData());
                    List<CategoryVO> temp = mCategoryList;
                    mCategoryAdapter.setNewData(temp);
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(rvCategory, "End of Product List", Snackbar.LENGTH_LONG).show();
                        } else {
                            retryDialog.show();
                            retryDialog.tvRetry.setText("No Internet Connection");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                getCategory();
                            });
                        }
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("No Internet Connection");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            getCategory();
                        });
                    }
                }
            });
        }
    }

    private void setupCategory() {
        mCategoryList = new ArrayList<>();
        CategoryVO allProduct = new CategoryVO();
        allProduct.setName("All Product");
        allProduct.setId((long) -2);
        CategoryVO promo = new CategoryVO();
        promo.setName("Promotion");
        promo.setId((long) -1);
        mCategoryList.add(allProduct);
        mCategoryList.add(promo);
    }

    private void setupViewPager(ArrayList<AdvertisementVO> mAdvertisementList) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(this.getSupportFragmentManager(), mAdvertisementList);
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

    public void launchWebView(String url) {
        if (url != null) {
            CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder()
                    .addDefaultShareMenuItem()
                    .setToolbarColor(this.getResources()
                            .getColor(R.color.colorPrimary))
                    .setShowTitle(true)
                    .setCloseButtonIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_arrow_back_white_24dp))
                    .build();

//      This is optional but recommended
            CustomTabsHelper.addKeepAliveExtra(this, customTabsIntent.intent);

//      This is where the magic happens...
            CustomTabsHelper.openCustomTab(this, customTabsIntent,
                    Uri.parse(url),
                    new WebViewFallback());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_menu, menu);
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
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        if (item.getItemId() == R.id.menu_search) {
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "search_item");
            mFirebaseAnalytics.logEvent("click_search", bundle);
            startActivity(ProductSearchActivity.newIntent(this));
            return true;
        } else if (item.getItemId() == R.id.menu_cart) {
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_cart");
            mFirebaseAnalytics.logEvent("click_cart", bundle);
            startActivity(CartActivity.newIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onItemClick(CategoryVO categoryVO) {
        Gson gson = new Gson();
        String json = gson.toJson(categoryVO);
        startActivity(ProductShowActivity.newIntent(this, json));
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start auto scroll when onResume
        viewPager.startAutoScroll();
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
        if (!scrollTop) {
            nestedScrollView.fling(0);
            nestedScrollView.smoothScrollTo(0, 0);
        } else {
            super.onBackPressed();
        }
    }
}
