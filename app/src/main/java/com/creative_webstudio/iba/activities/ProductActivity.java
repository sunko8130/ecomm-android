package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
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

import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.datas.vos.NamesVo;
import com.creative_webstudio.iba.fragments.FragmentOne;
import com.creative_webstudio.iba.fragments.FragmentTwo;
import com.creative_webstudio.iba.networks.ProductViewModel;
import com.viewpagerindicator.CirclePageIndicator;

import org.mmtextview.components.MMTextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;
import retrofit2.Response;

public class ProductActivity extends BaseDrawerActivity implements SearchView.OnQueryTextListener {

    @Nullable @BindView(R.id.rv_product)
    SmartRecyclerView rvProduct;

    @Nullable @BindView(R.id.btn_product)
    Button btnProduct;

    @Nullable @BindView(R.id.view_pager)
    AutoScrollViewPager viewPager;

    @Nullable @BindView(R.id.appbar)
    AppBarLayout appBar;

    @Nullable @BindView(R.id.ll)
    RelativeLayout ll;

    @Nullable @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    // Load more animation
    @Nullable @BindView(R.id.aniLoadMore)
    LottieAnimationView aniLoadMore;

    private SmartScrollListener mSmartScrollListener;

    //empty view

    @Nullable @BindView(R.id.vp_empty_product)
    EmptyViewPod vpEmpty;


    @Nullable @BindView(R.id.btn_refresh_empty)
    TextView tvEmpty;

    //no more Items

    @Nullable @BindView(R.id.tvNoMoreData)
    MMTextView tvNoMoreData;

    private ProductAdapter mProductAdapter;
    private List<NamesVo> names = new ArrayList<>();
    private CirclePageIndicator titlePageIndicator;

    private int mCurrentPage;
    private boolean mIsLoading;
    private boolean mIsNoMoreData;

    private String[] items = {"All Products", "Sport Drink", "Cold Drinks", "Coffee"};
    private String chooseItem;

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
        // TODO: specify the span count in res directory for phone and tablet size.
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        rvProduct.setLayoutManager(layoutManager);
        if (mProductAdapter.getItemCount() == 0) {
            appBar.setExpanded(false);
        } else {
            appBar.setExpanded(true);
        }


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });

        tvEmpty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });


        btnProduct.setOnClickListener(v -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
            builder.setTitle("Filter by");
            builder.setSingleChoiceItems(items, -1, (dialog, item) -> chooseItem = items[item]);
            builder.setPositiveButton("Ok", (dialog, which) -> btnProduct.setText(chooseItem));
            builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
            AlertDialog productDialog = builder.create();
            productDialog.show();

        });

        rvProduct.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mIsLoading && !mIsNoMoreData && layoutManager.findLastVisibleItemPosition() == mProductAdapter.getItemCount() - 1) {
                    getProduct(mCurrentPage + 1);
                    mIsLoading = true; // Prevent duplicate request while fetching from server
                }
            }
        });

        setupViewPager();
        getProduct(mCurrentPage++);
    }

    private void getProduct(int page) {
        ProductViewModel viewModel = ViewModelProviders.of(this).get(ProductViewModel.class);
        viewModel.getProduct(page).observe(this, apiResponse -> {
            aniLoadMore.setVisibility(View.GONE);

            if (apiResponse.getData() != null) {
                mCurrentPage++;
                mIsLoading = false;
                mProductAdapter.setNewData(apiResponse.getData().getProductVOList());
            } else {
                if (apiResponse.getError() instanceof ApiException) {
                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                    if (errorCode == 401) {
                        super.refreshAccessToken();
                    } else if (errorCode == 204) {
                        // TODO: Server response successful but there is no data (Empty response).
                    }
                } else {
                    // TODO: Network related error occurs. Show the retry button with status text so that user can retry.
                }
            }
        });
    }

    @Override
    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {
        getProduct(mCurrentPage);
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
        setCount(this, "99");
        return true;
    }

    private void setCount(Context context, String count) {
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

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_group_count, badge);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_search) {
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
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String userInput = newText.toLowerCase();
        List<NamesVo> mName = new ArrayList<>();
        for (NamesVo name : names) {
            if (name.getName().toLowerCase().contains(userInput)) {
                mName.add(name);
            }
        }

//        searchAdapter.setNewData(mName);
        return true;
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
    }

}
