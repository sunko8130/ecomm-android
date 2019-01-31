package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.customtabs.CustomTabsIntent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.creative_webstudio.iba.MyOnPageChangeListener;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.ProductAdapter;
import com.creative_webstudio.iba.adapters.SectionPagerAdapter;
import com.creative_webstudio.iba.components.CountDrawable;
import com.creative_webstudio.iba.components.EmptyViewPod;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.components.SmartScrollListener;

import com.creative_webstudio.iba.datas.vos.AdvertisementVO;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.ProductViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.mmtextview.components.MMTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Response;
import saschpe.android.customtabs.CustomTabsHelper;
import saschpe.android.customtabs.WebViewFallback;

public class ProductActivity extends BaseDrawerActivity {

    @Nullable
    @BindView(R.id.rv_product)
    SmartRecyclerView rvProduct;

    @Nullable
    @BindView(R.id.btn_product)
    Button btnProduct;

    @Nullable
    @BindView(R.id.view_pager)
    AutoScrollViewPager viewPager;

    @Nullable
    @BindView(R.id.appbar)
    AppBarLayout appBar;

    @Nullable
    @BindView(R.id.ll)
    RelativeLayout ll;

    @Nullable
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    // Load more animation
    @Nullable
    @BindView(R.id.aniLoadMore)
    LottieAnimationView aniLoadMore;

    private SmartScrollListener mSmartScrollListener;

    //empty view

    @Nullable
    @BindView(R.id.vp_empty_product)
    EmptyViewPod vpEmpty;

    @Nullable
    @BindView(R.id.btn_refresh_empty)
    TextView btnEmpty;

    @Nullable
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    //no more Items

    @Nullable
    @BindView(R.id.tvNoMoreData)
    MMTextView tvNoMoreData;

    private ProductAdapter mProductAdapter;
    private CirclePageIndicator titlePageIndicator;

    private int mCurrentPage = 1;
    private boolean mIsLoading;
    private boolean mIsNoMoreData;

    private String[] items = {"All Product","Promotion"};
    private String chooseItem;
    private int checkedItem = 0;

    //cart items
    private int cartItems = 0;


    //category id
    private int categoryId = -2;

    private ArrayList<CategoryVO> mCategoryList;

    private ArrayList<AdvertisementVO> mAdvertisementList;

    private ProductViewModel mProductViewModel;

    //Scroll top
    boolean scrollTop = true;

    GridLayoutManager layoutManager;

    //loading show
//    AlertDialog loadingDialog;


    public static Intent newIntent(Context context) {
        return new Intent(context, ProductActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyView(R.layout.activity_product);
        ButterKnife.bind(this, this);
        rvProduct.setEmptyView(vpEmpty);
        mProductAdapter = new ProductAdapter(this);
        mCategoryList = new ArrayList<>();
        mAdvertisementList = new ArrayList<>();
//        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Products.Please wait!");
        // TODO: specify the span count in res directory for phone and tablet size.
        layoutManager = new GridLayoutManager(this, 2);
        rvProduct.setLayoutManager(layoutManager);
        rvProduct.setAdapter(mProductAdapter);
        mProductViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        tvEmpty.setText("Loading Data........");
        btnEmpty.setVisibility(View.GONE);
//        getCategory();
        getAdvertisement();
        if (mProductAdapter.getItemCount() == 0) {
            appBar.setExpanded(false);
        } else {
            appBar.setExpanded(true);
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            categoryId = -2;
            checkedItem = 0;
            btnProduct.setText(items[0]);
            refreshData();
        });

        btnEmpty.setOnClickListener(v -> {
            categoryId = -2;
            refreshData();
        });


        rvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mIsLoading && !mIsNoMoreData && layoutManager.findLastVisibleItemPosition() == mProductAdapter.getItemCount() - 1) {
                    mIsLoading = true;
                    aniLoadMore.setVisibility(View.VISIBLE);// Prevent duplicate request while fetching from server
                    getProduct(mCurrentPage, categoryId);
                }
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    scrollTop = true;
                } else {
                    scrollTop = false;
                }
            }
        });
//        setupViewPager(mAdvertisementList);
//        getProduct(++mCurrentPage, categoryId);
    }

    private void refreshData() {
        collapse();
        tvEmpty.setText("Loading Data......");
        btnEmpty.setVisibility(View.GONE);
        if(categoryId==-1) {
            btnProduct.setText("Promotion");
        }else if(categoryId==-2) {
            btnProduct.setText("All Product");
        }
        checkedItem = categoryId+2;
//        if (!loadingDialog.isShowing()) {
//            loadingDialog.show();
//        }
        mProductAdapter.clearData();
        mIsLoading = true;
        mCurrentPage = 1;
        if (categoryId == -1 || categoryId==-2) {
            getProduct(mCurrentPage, categoryId);
        } else {
            getProduct(mCurrentPage, mCategoryList.get(categoryId).getId());
        }
    }

    private void getAdvertisement() {
//        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Advertisement.Please wait!");
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getAdvertisement();
            });
        } else {
//            loadingDialog.show();
            mProductViewModel.getAdvertisement().observe(this, apiResponse -> {
//                if (loadingDialog.isShowing()) {
//                    loadingDialog.dismiss();
//                }
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
                            AdvertisementVO advertisementVO=new AdvertisementVO();
                            mAdvertisementList.add(advertisementVO);
                            setupViewPager(mAdvertisementList);
                            getCategory();
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(rvProduct, "End of Product List", Snackbar.LENGTH_LONG).show();
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
//        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Products.Please wait!");
        if (!checkNetwork()) {
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getCategory();
            });
        } else {
//            loadingDialog.show();
            mProductViewModel.getCategory().observe(this, apiResponse -> {
                if (apiResponse.getData() != null) {
                    mCategoryList = (ArrayList<CategoryVO>) apiResponse.getData();
                    setupCategory();
                    getProduct(mCurrentPage, categoryId);
                } else {
//                    if (loadingDialog.isShowing()) {
//                        loadingDialog.dismiss();
//                    }
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(rvProduct, "End of Product List", Snackbar.LENGTH_LONG).show();
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
        items = new String[mCategoryList.size() + 2];
        items[0] = "All Product";
        items[1] = "Promotion";
        for (int i = 0; i < mCategoryList.size(); i++) {
            items[i + 2] = mCategoryList.get(i).getName();
        }
        btnProduct.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setTitle("Filter by");
            builder.setSingleChoiceItems(items, checkedItem, (dialog, item) -> {
                chooseItem = items[item];
                categoryId = item - 2;
                checkedItem = item;
            });
            builder.setPositiveButton("Ok", (dialog, which) -> {
                btnProduct.setText(chooseItem);
                mCurrentPage = 1;
                refreshData();
                Bundle bundle = new Bundle();
                bundle.putString("category_name", chooseItem);
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "search_with_category");
                mFirebaseAnalytics.logEvent("click_filter_ok", bundle);
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            AlertDialog productDialog = builder.create();
            productDialog.show();

        });
    }

    private void getProduct(int page, long productCategoryId) {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getProduct(page, productCategoryId);
            });
        } else {
            mProductViewModel.getProduct(page, productCategoryId).observe(this, apiResponse -> {
//                if (loadingDialog.isShowing()) {
//                    loadingDialog.dismiss();
//                }
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                aniLoadMore.setVisibility(View.GONE);
                if (apiResponse.getData() != null) {
                    if (mCurrentPage == 1) {
                        expand();
                    }
                    categoryId = (int) productCategoryId;
                    mCurrentPage++;
                    mIsLoading = false;
                    List<ProductVO> list = new ArrayList<>();
                    for (ProductVO productVO : apiResponse.getData().getProductVOList()) {
                        if (productVO.getStatus().equals("AVAILABLE")) {
                            list.add(productVO);
                        }
                    }
                    mProductAdapter.appendNewData(list);
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // TODO: Server response successful but there is no data (Empty response).
                            collapse();
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            if (mCurrentPage > 1) {
                                Snackbar.make(rvProduct, "End of Product List", Snackbar.LENGTH_LONG).show();
                            } else {
                                tvEmpty.setText("No Data to Display!");
                                btnEmpty.setVisibility(View.VISIBLE);
                                collapse();
                            }

                        } else {
                            retryDialog.show();
                            retryDialog.tvRetry.setText("No Internet Connection");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                getProduct(page, productCategoryId);
                            });
                        }
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("No Internet Connection");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            getProduct(page, productCategoryId);
                        });
                    }
                }
            });
        }
    }

    @Override
    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {
        getProduct(mCurrentPage, categoryId);
    }

    @Override
    public void onAccessTokenRefreshFailure(Throwable t) {
        if (t instanceof ApiException) {
            // Server response with one of the HTTP error status code.
            int errorCode = ((ApiException) t).getErrorCode();
            if (errorCode == 401) {
                // Refresh token is expired.
                startActivity(new Intent(this, SignInActivity.class));
            } else {
                // TODO: Show the retry button
                retryDialog.show();
                retryDialog.tvRetry.setText("Network Error");
                retryDialog.btnRetry.setOnClickListener(v -> {
                    retryDialog.dismiss();
                    super.refreshAccessToken();
                });
            }
        } else {
            // TODO: Show the retry button for network related error.
            retryDialog.show();
            retryDialog.tvRetry.setText("Network related Error");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                super.refreshAccessToken();
            });
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


    @Override
    protected void onPause() {
        super.onPause();
        // stop auto scroll when onPause
        viewPager.stopAutoScroll();
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
            layoutManager.smoothScrollToPosition(rvProduct, null, 0);
            expand();
        }else {
            super.onBackPressed();
        }
    }

    public void onItemClick(ProductVO data) {
        Bundle bundle = new Bundle();
        bundle.putLong("product_id", data.getId());
        bundle.putString("product_name", data.getProductName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_product_details");
        mFirebaseAnalytics.logEvent("click_product_item", bundle);
        Gson gson = new Gson();
        String json = gson.toJson(data);
        startActivity(ProductDetailsActivity.newIntent(this, "Product", json));
    }

    public void expand() {
//        final float scale = getResources().getDisplayMetrics().density;
//        int height = (int) (300 * scale);
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
//        params.height = height; // HEIGHT
//        appBar.setLayoutParams(params);
        appBar.setExpanded(true);
    }

    public void collapse() {
//        final float scale = getResources().getDisplayMetrics().density;
//        int height = (int) (0 * scale);
//        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
//        params.height = height; // HEIGHT
//        appBar.setLayoutParams(params);
        appBar.setExpanded(false);
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
}
