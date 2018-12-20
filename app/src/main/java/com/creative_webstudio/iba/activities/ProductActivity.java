package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
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

import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.fragments.FragmentOne;
import com.creative_webstudio.iba.fragments.FragmentTwo;
import com.creative_webstudio.iba.networks.viewmodels.ProductViewModel;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.google.gson.Gson;
import com.viewpagerindicator.CirclePageIndicator;

import org.mmtextview.components.MMTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Response;

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
    TextView tvEmpty;

    //no more Items

    @Nullable
    @BindView(R.id.tvNoMoreData)
    MMTextView tvNoMoreData;

    private ProductAdapter mProductAdapter;
    private CirclePageIndicator titlePageIndicator;

    private int mCurrentPage;
    private boolean mIsLoading;
    private boolean mIsNoMoreData;

    private String[] items = {"All Product"};
    private String chooseItem;
    private int checkedItem = -1;

    //cart items
    private int cartItems = 0;

    private IBAPreferenceManager ibaShared;

    //category id
    private int categoryId = -1;

    private ArrayList<CategoryVO> mCategoryList;

    private ProductViewModel mProductViewModel;


    public static Intent newIntent(Context context) {
        return new Intent(context, ProductActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyView(R.layout.activity_product);
        ButterKnife.bind(this, this);
        ibaShared = new IBAPreferenceManager(this);
        rvProduct.setEmptyView(vpEmpty);
        mProductAdapter = new ProductAdapter(this);
        mCategoryList = new ArrayList<>();
        // TODO: specify the span count in res directory for phone and tablet size.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProduct.setLayoutManager(layoutManager);
        rvProduct.setAdapter(mProductAdapter);
        mProductViewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        getCategory();
        if (mProductAdapter.getItemCount() == 0) {
            appBar.setExpanded(false);
        } else {
            appBar.setExpanded(true);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                categoryId = -1;
                checkedItem = -1;
                btnProduct.setText(items[0]);
                refreshData();
            }
        });

        tvEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getProduct(mCurrentPage, categoryId);
            }
        });

        rvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mIsLoading && !mIsNoMoreData && layoutManager.findLastVisibleItemPosition() == mProductAdapter.getItemCount() - 1) {
                    mIsLoading = true;
                    aniLoadMore.setVisibility(View.VISIBLE);// Prevent duplicate request while fetching from server
                    getProduct(mCurrentPage, categoryId);
//                    new Handler().postDelayed(new Runnable() {
//                        @Override
//                        public void run() {
//
//                        }
//                    }, 5000);
                }
            }
        });

        btnProduct.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setTitle("Filter by");
            builder.setSingleChoiceItems(items, checkedItem, (dialog, item) -> {
                chooseItem = items[item];
                categoryId = item - 1;
            });
            builder.setPositiveButton("Ok", (dialog, which) -> {
                btnProduct.setText(chooseItem);
                mCurrentPage = 1;
                refreshData();
            });
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            AlertDialog productDialog = builder.create();
            productDialog.show();

        });

        setupViewPager();
        getProduct(++mCurrentPage, categoryId);
    }

    private void refreshData() {
        mProductAdapter.clearData();
        mIsLoading = true;
        mCurrentPage = 1;
        if (categoryId == -1) {
            getProduct(mCurrentPage, categoryId);
        } else {
            getProduct(mCurrentPage,mCategoryList.get(categoryId).getId());
        }
    }

    private void getCategory() {
        mProductViewModel.getCategory().observe(this, apiResponse -> {
            if (apiResponse.getData() != null) {
                mCategoryList = (ArrayList<CategoryVO>) apiResponse.getData();
                setupCategory();
            } else {
                if (apiResponse.getError() instanceof ApiException) {
                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                    if (errorCode == 401) {
                        super.refreshAccessToken();
                    } else if (errorCode == 204) {
                        // TODO: Server response successful but there is no data (Empty response).
                    } else if (errorCode == 200) {
                        // TODO: Reach End of List
                        Snackbar.make(rvProduct, "End of Product List", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    // TODO: Network related error occurs. Show the retry button with status text so that user can retry.
                }
            }
        });
    }

    private void setupCategory() {
        items = new String[mCategoryList.size() + 1];
        items[0] = "All Product";
        for (int i = 0; i < mCategoryList.size(); i++) {
            items[i + 1] = mCategoryList.get(i).getName();
        }
    }

    private void getProduct(int page, long productCategoryId) {
        mProductViewModel.getProduct(page, productCategoryId).observe(this, apiResponse -> {
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
                mProductAdapter.appendNewData(apiResponse.getData().getProductVOList());
            } else {
                if (apiResponse.getError() instanceof ApiException) {
                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                    if (errorCode == 401) {
                        super.refreshAccessToken();
                    } else if (errorCode == 204) {
                        // TODO: Server response successful but there is no data (Empty response).
                    } else if (errorCode == 200) {
                        // TODO: Reach End of List
                        Snackbar.make(rvProduct, "End of Product List", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    // TODO: Network related error occurs. Show the retry button with status text so that user can retry.
                }
            }
        });
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
            }
        } else {
            // TODO: Show the retry button for network related error.
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
        if (item.getItemId() == R.id.menu_search) {
            ibaShared.removePreference("CartList");
            startActivity(ProductSearchActivity.newIntent(this));
            return true;
        } else if (item.getItemId() == R.id.menu_cart) {
            startActivity(CartActivity.newIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupViewPager() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne());
        adapter.addFragment(new FragmentTwo());
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

    public void onItemClick(ProductVO data) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        startActivity(ProductDetailsActivity.newIntent(this, "Product", json));
    }

    public void expand() {
        final float scale = getResources().getDisplayMetrics().density;
        int height = (int) (300 * scale);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
        params.height = height; // HEIGHT
        appBar.setLayoutParams(params);
        appBar.setExpanded(true);
    }
}
