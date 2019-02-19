package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.ProductAdapter;
import com.creative_webstudio.iba.adapters.SubCategoryAdapter;
import com.creative_webstudio.iba.components.CountDrawable;
import com.creative_webstudio.iba.components.EmptyViewPod;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.ProductViewModel;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.mmtextview.components.MMTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductShowActivity extends BaseActivity {

    private int cartItems = 0;
    //Toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.rcSubCate)
    RecyclerView rcSubCate;

    @Nullable
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @BindView(R.id.rv_product)
    SmartRecyclerView rvProduct;

    @Nullable
    @BindView(R.id.aniLoadMore)
    LottieAnimationView aniLoadMore;

    @Nullable
    @BindView(R.id.nestedScroll)
    NestedScrollView nestedScrollView;

    @Nullable
    @BindView(R.id.subHeader)
    MMTextView subHeader;

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
    private CategoryVO categoryVO;

    private IBAPreferenceManager ibaShared;

    private ProductViewModel mProductViewModel;

    private ArrayList<CategoryVO> mCategoryList;

    private SubCategoryAdapter mSubAdapter;

    //for product

    private int mCurrentPage = 1;
    private boolean mIsLoading;
    boolean scrollTop = true;
    //category id
    private Long categoryId = Long.valueOf(0);

    GridLayoutManager layoutManager;
    private ProductAdapter mProductAdapter;

    public static Intent newIntent(Context context, String categoryVo) {
        Intent intent = new Intent(context, ProductShowActivity.class);
        intent.putExtra("categoryVo", categoryVo);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_show);
        ButterKnife.bind(this, this);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        ibaShared = new IBAPreferenceManager(this);
        mProductViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        mCategoryList = new ArrayList<>();
        mSubAdapter = new SubCategoryAdapter(this);
        rcSubCate.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rcSubCate.setAdapter(mSubAdapter);
        rvProduct.setEmptyView(vpEmpty);
        mProductAdapter = new ProductAdapter(this);
        if (isTablet(this)) {
            layoutManager = new GridLayoutManager(this, 3);
        } else {
            layoutManager = new GridLayoutManager(this, 2);
        }
        rvProduct.setLayoutManager(layoutManager);
        rvProduct.setAdapter(mProductAdapter);
        tvEmpty.setText("Loading Data........");
        btnEmpty.setVisibility(View.GONE);
        if (getIntent().hasExtra("categoryVo")) {
            String json = getIntent().getStringExtra("categoryVo");
            Gson gson = new Gson();
            categoryVO = gson.fromJson(json, CategoryVO.class);
            toolbar.setTitle(categoryVO.getName());
            getSubCategory(categoryVO.getId());
            if (categoryVO.getId() > 3) {
                CategoryVO temp = categoryVO;
                temp.setName("All " + categoryVO.getName());
                mCategoryList.add(categoryVO);
                categoryId = categoryVO.getId();
                categoryVO.setSelected(true);
            } else {
                categoryId = categoryVO.getId();
                subHeader.setVisibility(View.GONE);
            }
        }

        swipeRefreshLayout.setOnRefreshListener(() -> refreshData());

        btnEmpty.setOnClickListener(v -> refreshData());

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (scrollY == 0) {
                scrollTop = true;
            } else {
                scrollTop = false;
            }
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) &&
                        scrollY > oldScrollY) {
                    int visibleItemCount = layoutManager.getChildCount();
                    int totalItemCount = layoutManager.getItemCount();
                    int pastVisibleItems = layoutManager.findFirstVisibleItemPosition();
                    if (!mIsLoading) {
                        mIsLoading = true;
                        if ((visibleItemCount + pastVisibleItems) >= totalItemCount) {
                            aniLoadMore.setVisibility(View.VISIBLE);// Prevent duplicate request while fetching from server
                            getProduct(mCurrentPage, categoryId);
                        }
                    }
                }
            }
        });
    }

    public boolean isTablet(Context context) {
        boolean xlarge = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == 4);
        boolean large = ((context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE);
        return (xlarge || large);
    }

    private void refreshData() {
//        collapse();
        tvEmpty.setText("Loading Data......");
        btnEmpty.setVisibility(View.GONE);
//        if (categoryId == -1) {
//            btnProduct.setText("Promotion");
//        } else if (categoryId == -2) {
//            btnProduct.setText("All Product");
//        }
        mProductAdapter.clearData();
        mIsLoading = true;
        mCurrentPage = 1;
        getProduct(mCurrentPage, categoryId);

    }

    private void getSubCategory(Long categoryId) {
        if (!checkNetwork()) {
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getSubCategory(categoryId);
            });
        } else {
            mProductViewModel.getSubCategory(categoryId).observe(this, apiResponse -> {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                if (apiResponse.getData() != null) {
                    mCategoryList.addAll(apiResponse.getData());
                    mSubAdapter.setNewData(mCategoryList);
                    getProduct(mCurrentPage, categoryId);
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            mSubAdapter.setNewData(mCategoryList);
                            getProduct(mCurrentPage, categoryId);
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(toolbar, "End of Product List", Snackbar.LENGTH_LONG).show();
                        } else {
                            retryDialog.show();
                            retryDialog.tvRetry.setText("No Internet Connection");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                getSubCategory(categoryId);
                            });
                        }
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("No Internet Connection");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            getSubCategory(categoryId);
                        });
                    }
                }
            });
        }
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
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                aniLoadMore.setVisibility(View.GONE);
                if (apiResponse.getData() != null) {
                    if (mCurrentPage == 1) {
//                        expand();
                    }
//                    categoryId = (int) productCategoryId;
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
//                            collapse();
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            if (mCurrentPage > 1) {
                                Snackbar.make(rvProduct, "End of Product List", Snackbar.LENGTH_LONG).show();
                            } else {
                                tvEmpty.setText("No Data to Display!");
                                btnEmpty.setVisibility(View.VISIBLE);
//                                collapse();
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

    public void onSubItemClick(CategoryVO categoryVO) {
        mProductAdapter.clearData();
        tvEmpty.setText("Loading data........");
        btnEmpty.setVisibility(View.GONE);
        mCurrentPage = 1;
        categoryId = categoryVO.getId();
        getProduct(mCurrentPage, categoryId);
        for (int i = 0; i < mCategoryList.size(); i++) {
            if (mCategoryList.get(i).getId().equals(categoryId)) {
                mCategoryList.get(i).setSelected(true);
            } else {
                mCategoryList.get(i).setSelected(false);
            }
        }
        mSubAdapter.setNewData(mCategoryList);
    }

    public void onProductItemClick(ProductVO data) {
        Bundle bundle = new Bundle();
        bundle.putLong("product_id", data.getId());
        bundle.putString("product_name", data.getProductName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_product_details");
        mFirebaseAnalytics.logEvent("click_product_item", bundle);
        Gson gson = new Gson();
        String json = gson.toJson(data);
        startActivity(ProductDetailsActivity.newIntent(this, "Product", json));
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
        refreshData();
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
