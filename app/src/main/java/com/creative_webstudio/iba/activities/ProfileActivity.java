package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.CustomerVO;

import org.mmtextview.components.MMTextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends BaseActivity {

    @BindView(R.id.tvName)
    MMTextView tvName;

    @BindView(R.id.tvEmail)
    MMTextView tvEmail;

    @BindView(R.id.tvPhone)
    MMTextView tvPhone;

    @BindView(R.id.tvAddress)
    MMTextView tvAddress;

    @BindView(R.id.tvTownShip)
    MMTextView tvTownShip;

    @BindView(R.id.tvDivision)
    MMTextView tvDivision;

    @BindView(R.id.tvShopName)
    MMTextView tvShopName;

    private CustomerVO customerVO;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ProfileActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this,this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        customerVO = IbaModel.getInstance().getCustomerVO();
        tvName.setText(customerVO.getName());
        tvEmail.setText(customerVO.getEmail());
        tvPhone.setText(customerVO.getPhone());
        tvTownShip.setText(customerVO.getTownShip());
        tvDivision.setText(customerVO.getDivision());
        tvShopName.setText(customerVO.getShopName());
        tvAddress.setText(customerVO.getAddress());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
