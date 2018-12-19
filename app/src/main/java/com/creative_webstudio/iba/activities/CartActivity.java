package com.creative_webstudio.iba.activities;

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
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.CriteriaVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CartActivity extends BaseActivity {
    @Nullable
    @BindView(R.id.rv_cart_list)
    RecyclerView rvCart;

    private CartAdapter mCartAdapter;
    private List<CartVO> cartVOList;
    private IBAPreferenceManager ibaShared;
    private CriteriaVO criteriaVO;

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
        ibaShared = new IBAPreferenceManager(this);
        mCartAdapter= new CartAdapter(this);
        rvCart.setAdapter(mCartAdapter);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        if(ibaShared.getItemsFromCart()!=null){
            cartVOList=ibaShared.getItemsFromCart();
        }else {
            cartVOList = new ArrayList<>();
        }
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
        super.onBackPressed();
//        startActivity(ProductActivity.newIntent(this));
    }
}
