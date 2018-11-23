package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.ProductAdapter;
import com.creative_webstudio.iba.adapters.SearchAdapter;
import com.creative_webstudio.iba.adapters.SectionPagerAdapter;
import com.creative_webstudio.iba.components.CountDrawable;
import com.creative_webstudio.iba.vos.NamesVo;
import com.creative_webstudio.iba.delegates.ProductDelegate;
import com.creative_webstudio.iba.fragments.FragmentOne;
import com.creative_webstudio.iba.fragments.FragmentTwo;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public class ProductActivity extends BaseDrawerActivity implements ProductDelegate, SearchView.OnQueryTextListener {
    RecyclerView rvProduct, rvSearch;
    Button btnProduct;
    AlertDialog productDialog;
    TextView tvFilterBy;
    ViewPager viewPager;
    AppBarLayout appBar;


    LinearLayout ll, llSearch;
    private ProductAdapter productAdapter;
    private List<NamesVo> names = new ArrayList<>();
    CirclePageIndicator titlePageIndicator;
    SearchAdapter searchAdapter;


    private String[] items = {"All Products", "Sport Drink", "Cold Drinks", "Coffee"};
    private String chooseItem;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ProductActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyView(R.layout.activity_product);
        ButterKnife.bind(this, this);

        rvSearch = findViewById(R.id.rv_search);
        rvProduct = findViewById(R.id.rv_product);
        ll = findViewById(R.id.ll);
        llSearch = findViewById(R.id.ll_search);
        btnProduct = findViewById(R.id.btn_product);
        tvFilterBy = findViewById(R.id.tv_filter);
        appBar = findViewById(R.id.appbar);

        NamesVo namesVo = new NamesVo("start");
        names = namesVo.getNames();



        productAdapter = new ProductAdapter(this, names);
        rvProduct.setAdapter(productAdapter);
        rvProduct.setLayoutManager(new GridLayoutManager(this, 2));

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
        MenuItem menuItem=toolbar.getMenu().findItem(R.id.menu_cart);
        LayerDrawable icon = (LayerDrawable) menuItem.getIcon();

        CountDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_group_count);
        if (reuse != null && reuse instanceof CountDrawable) {
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
        if(item.getItemId()==R.id.menu_search){
            onTapSearch();
            return true;
        }else if(item.getItemId()==R.id.menu_cart){
            startActivity(CartActivity.newIntent(this));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTapView() {
        startActivity(ProductDetailsActivity.newIntent(this,"Product"));
        overridePendingTransition(R.anim.rotate_clockwise_anim, R.anim.zoom_out_anim);
    }

    @Override
    public void onTapSearch() {
        Intent i = new Intent(this, ProductSearchActivity.class);
        startActivity(i);

    }

    @Override
    public void onTapShoppingCart() {

    }


    private void setupViewPager() {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentOne());
        adapter.addFragment(new FragmentTwo());
        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
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

        searchAdapter.updateProductList(mName);
        return true;
    }


}
