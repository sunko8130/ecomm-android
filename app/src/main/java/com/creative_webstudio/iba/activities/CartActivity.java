package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.CartAdapter;
import com.creative_webstudio.iba.delegates.CartDelegate;
import com.creative_webstudio.iba.vos.NamesVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Optional;

/**
 * Created by DELL on 11/19/2018.
 */

public class CartActivity extends BaseDrawerActivity implements CartDelegate {
    @Nullable
    @BindView(R.id.rv_card_list)
    RecyclerView rvCard;
    List<NamesVo> names = new ArrayList<>();

    public static Intent newIntent(Context context){
        Intent intent=new Intent(context,CartActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setMyView(R.layout.activity_cart);
        ButterKnife.bind(this, this);
        NamesVo namesVo = new NamesVo("start");
        names = namesVo.getNames();
        CartAdapter cartAdapter = new CartAdapter(this, names);
        rvCard.setAdapter(cartAdapter);
        rvCard.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onTapView() {
        startActivity(ProductDetailsActivity.newIntent(this,"Cart"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
    }
}
