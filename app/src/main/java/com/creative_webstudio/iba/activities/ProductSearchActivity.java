package com.creative_webstudio.iba.activities;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


import com.airbnb.lottie.LottieAnimationView;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.SearchAdapter;
import com.creative_webstudio.iba.components.EmptyViewPod;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.datas.criterias.OrderUnitCriteria;
import com.creative_webstudio.iba.datas.criterias.ProductCriteria;
import com.creative_webstudio.iba.datas.criterias.ThumbnailCriteria;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.mvp.presenters.ProductSearchPresenter;
import com.creative_webstudio.iba.mvp.views.ProductSearchView;
import com.creative_webstudio.iba.networks.viewmodels.ProductSearchViewModel;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class ProductSearchActivity extends BaseActivity implements ProductSearchView, SearchView.OnQueryTextListener {

    @BindView(R.id.search_product_toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_search)
    SmartRecyclerView rvSearch;
    @BindView(R.id.search_view)
    SearchView searchView;
    //search loading
    @BindView(R.id.loadingSearch)
    LottieAnimationView loadingSearch;

    //empty view
    @Nullable
    @BindView(R.id.vp_empty_search)
    EmptyViewPod vpEmpty;

    SearchAdapter searchAdapter;

    ProductCriteria criteriaVO;
    private ProductSearchPresenter mPresenter;
    private List<ProductVO> mProductVOS;
    private ProductSearchViewModel mProductSearchViewModel;

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
        loadingSearch.setVisibility(View.GONE);

        mPresenter = ViewModelProviders.of(this).get(ProductSearchPresenter.class);
        mPresenter.initPresenter(this);

        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        searchAdapter = new SearchAdapter(this);
        //searchAdapter.setNewData(names);
        rvSearch.setAdapter(searchAdapter);
        rvSearch.setLayoutManager(new LinearLayoutManager(this));
        vpEmpty.setRefreshButton(false);
        vpEmpty.setEmptyData("There is no match found!");
        vpEmpty.setVisibility(View.GONE);

        mProductSearchViewModel = ViewModelProviders.of(this).get(ProductSearchViewModel.class);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        searchView.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
        searchView.setQueryHint("Search Product");
        searchView.setOnQueryTextListener(this);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

    }


    @Override
    public void onTapView() {
        startActivity(ProductDetailsActivity.newIntent(this, "Search"));
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        String userInput = query.toLowerCase();
        criteriaVO = new ProductCriteria();
        criteriaVO.setWord(userInput);
        criteriaVO.setPageNumber(null);
        criteriaVO.setWithOrderUnit(true);
        criteriaVO.setWithThumbnail(true);
        criteriaVO.setWithDetail(true);
        ThumbnailCriteria thumbnailCriteria=new ThumbnailCriteria();
        thumbnailCriteria.setThumbnailType(1);
        criteriaVO.setThumbnail(thumbnailCriteria);
        OrderUnitCriteria orderUnitCriteria = new OrderUnitCriteria();
        orderUnitCriteria.setWithPromoReward(true);
        criteriaVO.setOrderUnit(orderUnitCriteria);
        Bundle bundle = new Bundle();
        bundle.putString("search_dey", userInput);
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "search_product");
        mFirebaseAnalytics.logEvent("click_search_icon", bundle);
        getProductSearch(criteriaVO);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
    }

    public void getProductSearch(ProductCriteria criteriaVO) {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getProductSearch(criteriaVO);
            });
        } else {
            searchAdapter.clearData();
            loadingSearch.setVisibility(View.VISIBLE);
            mProductSearchViewModel.getProductSearchList(criteriaVO).observe(this, apiResponse -> {
                loadingSearch.setVisibility(View.GONE);
                if (apiResponse.getData() != null) {
                    List<ProductVO> list=new ArrayList<>();
                    for(ProductVO productVO:apiResponse.getData()){
                        if(productVO.getStatus().equals("AVAILABLE")){
                            list.add(productVO);
                        }
                    }
                    searchAdapter.setNewData(list);
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            rvSearch.setEmptyView(vpEmpty);
                            vpEmpty.setVisibility(View.VISIBLE);
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(rvSearch, "End of Product List", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("No Internet Connection");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            getProductSearch(criteriaVO);
                        });
                    }
                }
            });
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
        finish();
        startActivity(ProductDetailsActivity.newIntent(this, "ProductSearch", json));
    }

    @Override
    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {
        getProductSearch(criteriaVO);
    }
}
