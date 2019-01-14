package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.CustomerVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.utils.CustomDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

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
    //loading dialog
    AlertDialog loadingDialog;


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
    }

    @Override
    protected void onResume() {
        setUpData();
        super.onResume();
    }

    private void setUpData() {
        customerVO = IbaModel.getInstance().getCustomerVO();
        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Customer Information.Please wait!");
        loadingDialog.show();
        IbaModel.getInstance().getCustomer().observe(this, apiResponse->{
            if(loadingDialog.isShowing()){
                loadingDialog.dismiss();
            }
            if (apiResponse.getData() != null) {
                customerVO = apiResponse.getData();
                setCustomerValue(customerVO);
            } else {
                if (apiResponse.getError() instanceof ApiException) {
                    Crashlytics.logException(apiResponse.getError());
                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                    if (errorCode == 401) {
                        super.refreshAccessToken();
                    } else if (errorCode == 204) {
                        // TODO: Server response successful but there is no data (Empty response).
                    } else if (errorCode == 200) {
                        // TODO: Reach End of List
                    }
                } else {
                    retryDialog.show();
                    retryDialog.tvRetry.setText("No Internet Connection");
                    retryDialog.btnRetry.setOnClickListener(v -> {
                        retryDialog.dismiss();
                        setUpData();
                    });
                }
            }
        });
    }

    private void setCustomerValue(CustomerVO customerVO) {
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
        }else if(item.getItemId() == R.id.menu_edit){
            startActivity(ProfileEditActivity.newIntent(this));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu, menu);
        return true;
    }
}
