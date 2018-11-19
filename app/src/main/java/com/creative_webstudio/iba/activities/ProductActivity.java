package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.ProductAdapter;
import com.creative_webstudio.iba.adapters.SearchAdapter;
import com.creative_webstudio.iba.adapters.SectionPagerAdapter;
import com.creative_webstudio.iba.vos.NamesVo;
import com.creative_webstudio.iba.delegates.ProductDelegate;
import com.creative_webstudio.iba.fragments.FragmentOne;
import com.creative_webstudio.iba.fragments.FragmentTwo;
import com.creative_webstudio.iba.utils.ItemOffsetDecoration;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;
import butterknife.ButterKnife;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductActivity extends BaseDrawerActivity implements ProductDelegate, SearchView.OnQueryTextListener {
    RecyclerView rvProduct, rvSearch;
    Button btnProduct;
    AlertDialog productDialog;
    TextView tvFilterBy, tvItemCount;
    ViewPager viewPager;
    AppBarLayout appBar;
    @BindView(R.id.iv_search)
    ImageView ivSearch;


    LinearLayout ll, llSearch;
    android.support.v7.widget.Toolbar toolbar;
    private ProductAdapter productAdapter;
    private List<NamesVo> names = new ArrayList<>();
    CirclePageIndicator titlePageIndicator;
    SearchAdapter searchAdapter;


    private String[] items = {"All Products", "Sport Drink", "Cold Drinks", "Coffee"};
    private String chooseItem;

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,ProductActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyView(R.layout.activity_product);
        ButterKnife.bind(this,this);

        rvSearch = findViewById(R.id.rv_search);
        rvProduct = findViewById(R.id.rv_product);
        toolbar = findViewById(R.id.toolbar);
        ll = findViewById(R.id.ll);
        llSearch = findViewById(R.id.ll_search);
        btnProduct = findViewById(R.id.btn_product);
        tvFilterBy = findViewById(R.id.tv_filter);
        tvItemCount = findViewById(R.id.tv_item_count);
        appBar = findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        NamesVo namesVo = new NamesVo("start");
        names = namesVo.getNames();

        tvItemCount.setText("99");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_menu_white_24dp));

        productAdapter = new ProductAdapter(this, names);
        rvProduct.setAdapter(productAdapter);
        rvProduct.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(getApplicationContext(), R.dimen.margin_small);
        rvProduct.addItemDecoration(itemDecoration);

//        searchAdapter = new SearchAdapter(this, names);
//        rvSearch.setAdapter(searchAdapter);
//        rvSearch.setLayoutManager(new LinearLayoutManager(this));


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

        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTapSearch();
            }
        });
    }

    @Override
    public void onTapView() {
        Intent i = new Intent(this, ProductDetailsActivity.class);
        startActivity(i);
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
