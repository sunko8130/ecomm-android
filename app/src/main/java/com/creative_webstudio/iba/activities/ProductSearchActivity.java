package com.creative_webstudio.iba.activities;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.SearchAdapter;
import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.mvp.presenters.ProductSearchPresenter;
import com.creative_webstudio.iba.mvp.views.ProductSearchView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProductSearchActivity extends AppCompatActivity implements ProductSearchView, SearchView.OnQueryTextListener {

    @BindView(R.id.search_product_toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_search)
    RecyclerView rvSearch;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.tv_result)
    TextView tvResult;

    SearchAdapter searchAdapter;
    private ProductSearchPresenter mPresenter;

    private List<ProductVO> mProductVOS;

    public static Intent newIntent(Context context) {
        return new Intent(context, ProductSearchActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        ButterKnife.bind(this, this);
        setSupportActionBar(toolbar);
        mProductVOS = new ArrayList<>();

        mPresenter = ViewModelProviders.of(this).get(ProductSearchPresenter.class);
        mPresenter.initPresenter(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        searchAdapter = new SearchAdapter(this, mPresenter);
        //searchAdapter.setNewData(names);
        rvSearch.setAdapter(searchAdapter);
        rvSearch.setLayoutManager(new LinearLayoutManager(this));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        searchView.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
        searchView.setQueryHint("Search Product");
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        tvResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onclickTvResult", "onClick: Success ");
                mPresenter.onTapSearch("e");
                mPresenter.getmListMutableLiveData().observe(ProductSearchActivity.this, new Observer<List<ProductVO>>() {
                    @Override
                    public void onChanged(@Nullable List<ProductVO> productVOS) {
                        searchAdapter.setNewData(productVOS);
                    }
                });
            }
        });

    }


    @Override
    public void onTapView() {
        Intent i = new Intent(this, ProductDetailsActivity.class);
        startActivity(ProductDetailsActivity.newIntent(this, "Search"));
//        overridePendingTransition(R.anim.rotate_clockwise_anim, R.anim.zoom_out_anim);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        String userInput = newText.toLowerCase();
        mPresenter.onTapSearch(userInput);
        mPresenter.getmListMutableLiveData().observe(ProductSearchActivity.this, new Observer<List<ProductVO>>() {
            @Override
            public void onChanged(@Nullable List<ProductVO> productVOS) {
                searchAdapter.setNewData(productVOS);
                mProductVOS = productVOS;
            }
        });
        List<ProductVO> productVo = new ArrayList<>();
        for (ProductVO product : mProductVOS) {
            if (product.getProductName().toLowerCase().contains(userInput)) {
                productVo.add(product);
            }
        }

        searchAdapter.setNewData(productVo);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//       startActivity(ProductActivity.newIntent(this));
    }
}
