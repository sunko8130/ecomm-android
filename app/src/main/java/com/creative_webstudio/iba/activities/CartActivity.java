package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.CartAdapter;
import com.creative_webstudio.iba.datas.vos.NamesVo;
import com.creative_webstudio.iba.mvp.presenters.CartPresenter;
import com.creative_webstudio.iba.mvp.views.CartView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by DELL on 11/19/2018.
 */

public class CartActivity extends BaseActivity implements CartView {
    @Nullable
    @BindView(R.id.rv_card_list)
    RecyclerView rvCard;
    List<NamesVo> names = new ArrayList<>();
    private CartPresenter mPresenter;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CartActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this, this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        mPresenter = ViewModelProviders.of(this).get(CartPresenter.class);
        mPresenter.initPresenter(this);
        NamesVo namesVo = new NamesVo("start");
        names = namesVo.getNames();
        CartAdapter cartAdapter = new CartAdapter(names, mPresenter);
        rvCard.setAdapter(cartAdapter);
        rvCard.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onTapView() {
        startActivity(ProductDetailsActivity.newIntent(this, "Cart"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
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
        startActivity(ProductActivity.newIntent(this));
    }
}
