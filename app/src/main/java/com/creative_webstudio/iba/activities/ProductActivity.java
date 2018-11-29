package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.creative_webstudio.iba.MyOnPageChangeListener;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.ProductAdapter;
import com.creative_webstudio.iba.adapters.SearchAdapter;
import com.creative_webstudio.iba.adapters.SectionPagerAdapter;
import com.creative_webstudio.iba.components.CountDrawable;
import com.creative_webstudio.iba.components.EmptyViewPod;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.components.SmartScrollListener;
import com.creative_webstudio.iba.mvp.presenters.ProductPresenter;
import com.creative_webstudio.iba.mvp.views.ProductView;
import com.creative_webstudio.iba.datas.vos.NamesVo;
import com.creative_webstudio.iba.fragments.FragmentOne;
import com.creative_webstudio.iba.fragments.FragmentTwo;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

public class ProductActivity extends BaseDrawerActivity implements SearchView.OnQueryTextListener, ProductView {

    @Nullable
    @BindView(R.id.rv_product)
    SmartRecyclerView rvProduct;
    @Nullable
    @BindView(R.id.btn_product)
    Button btnProduct;

    AlertDialog productDialog;
    @Nullable
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @Nullable
    @BindView(R.id.appbar)
    AppBarLayout appBar;
    @Nullable
    @BindView(R.id.ll)
    RelativeLayout ll;

    //for swipe refresh
    @Nullable
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private SmartScrollListener mSmartScrollListener;

    //empty view
    @Nullable
    @BindView(R.id.vp_empty_product)
    EmptyViewPod vpEmpty;


    private ProductAdapter productAdapter;
    private List<NamesVo> names = new ArrayList<>();
    CirclePageIndicator titlePageIndicator;
    SearchAdapter searchAdapter;

    private ProductPresenter mPresenter;


    private String[] items = {"All Products", "Sport Drink", "Cold Drinks", "Coffee"};
    private String chooseItem;

    public static Intent newIntent(Context context) {
        return new Intent(context, ProductActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyView(R.layout.activity_product);
        ButterKnife.bind(this, this);
        mPresenter = ViewModelProviders.of(this).get(ProductPresenter.class);
        mPresenter.initPresenter(this);
        NamesVo namesVo = new NamesVo("start");
        names = namesVo.getNames();


        rvProduct.setEmptyView(vpEmpty);
        productAdapter = new ProductAdapter(this, mPresenter);
//        productAdapter.setNewData(names);
        rvProduct.setAdapter(productAdapter);
        rvProduct.setLayoutManager(new GridLayoutManager(this, 2));

        if(productAdapter.getItemCount()==0){
            appBar.setExpanded(false);
        }else {
            appBar.setExpanded(true);
        }
        mSmartScrollListener = new SmartScrollListener(new SmartScrollListener.OnSmartScrollListener() {
            @Override
            public void onListEndReach() {
                Snackbar.make(rvProduct, "Loading new data.", Snackbar.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(true);

//                mPresenter.onNewsListEndReach(getApplicationContext());
            }
        });

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                final float scale = getResources().getDisplayMetrics().density;
//                int height  = (int) (10 * scale);
//                CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
//                params.height = height; // HEIGHT
//                appBar.setLayoutParams(params);
//                appBar.setExpanded(true);
//                viewPager.setVisibility(View.GONE);
                Snackbar.make(rvProduct, "Refreshing new data.", Snackbar.LENGTH_LONG).show();
                productAdapter.setNewData(names);
                swipeRefreshLayout.setRefreshing(false);
                //expand
                final float scale = getResources().getDisplayMetrics().density;
                int height  = (int) (300 * scale);
                CoordinatorLayout.LayoutParams params =(CoordinatorLayout.LayoutParams) appBar.getLayoutParams();
                params.height = height; // HEIGHT
                appBar.setLayoutParams(params);
                appBar.setExpanded(true);
//                mPresenter.onForceRefresh(getApplicationContext());
            }
        });

        btnProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ProductActivity.this);
                builder.setTitle("Filter by");
                builder.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        chooseItem = items[item];
                    }

                });
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        btnProduct.setText(chooseItem);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productDialog.dismiss();
                    }
                });
                productDialog = builder.create();
                productDialog.show();

            }
        });
        setupViewPager();
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
            onTapSearch();
            return true;
        } else if (item.getItemId() == R.id.menu_cart) {
            startActivity(CartActivity.newIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTapView() {
        startActivity(ProductDetailsActivity.newIntent(this, "Product"));
//        overridePendingTransition(R.anim.rotate_clockwise_anim, R.anim.zoom_out_anim);
    }

    @Override
    public void onTapSearch() {
        startActivity(ProductSearchActivity.newIntent(this));
    }

    @Override
    public void onTapShoppingCart() {

    }
    private void setupViewPager() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne());
        adapter.addFragment(new FragmentTwo());
        if (viewPager != null) {
            viewPager.setAdapter(adapter);
        }
        viewPager.addOnPageChangeListener(new MyOnPageChangeListener());
        viewPager.setInterval(2000);
        viewPager.startAutoScroll();
        titlePageIndicator = findViewById(R.id.title_page_indicator);
        titlePageIndicator.setViewPager(viewPager);
        titlePageIndicator.setSnap(true);
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

        searchAdapter.setNewData(mName);
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
