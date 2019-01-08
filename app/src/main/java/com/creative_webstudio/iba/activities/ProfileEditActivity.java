package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.datas.criterias.CustomerCriteria;
import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.CustomerVO;
import com.creative_webstudio.iba.datas.vos.RegionVO;
import com.creative_webstudio.iba.datas.vos.TownshipVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.ProfielEditViewModel;
import com.creative_webstudio.iba.utils.CustomDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileEditActivity extends  BaseActivity implements View.OnClickListener {

    @BindView(R.id.btnOk)
    Button btnOk;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.etName)
    TextInputEditText etName;

    @BindView(R.id.etPhone)
    TextInputEditText etPhone;

    @BindView(R.id.etAddress)
    TextInputEditText etAddress;

    @BindView(R.id.etEmail)
    TextInputEditText etEmail;

    @BindView(R.id.spDivision)
    Spinner spDivision;

    @BindView(R.id.spTownship)
    Spinner spTownship;

    //loading dialog
    AlertDialog loadingDialog;

    List<RegionVO> regionVOList;

    List<TownshipVO> townshipVOList;

    String selectedRegion=null;

    String selectedTownship=null;

    CustomerVO customerVO;
    private ProfielEditViewModel editViewModel;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, ProfileEditActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        ButterKnife.bind(this,this);
        customerVO = IbaModel.getInstance().getCustomerVO();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        editViewModel = ViewModelProviders.of(this).get(ProfielEditViewModel.class);
        etName.setText(customerVO.getName());
        etPhone.setText(customerVO.getPhone());
        etAddress.setText(customerVO.getAddress());
        etEmail.setText(customerVO.getEmail());
        getRegion();

        btnOk.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    private void getRegion() {
        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Set up Regions.Please wait!");
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getRegion();
            });
        } else{
            loadingDialog.show();
            editViewModel.getRegion().observe(this, apiResponse -> {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (apiResponse.getData() != null) {
                    regionVOList = apiResponse.getData();
                    setUpRegion();
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(btnOk, "End of Product List", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("Network error!");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            getRegion();
                        });
                    }
                }
            });
        }
    }

    private void getTownShip(String code) {
        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Set up Township.Please wait!");
        loadingDialog.show();
        editViewModel.getTownship(code).observe(this, apiResponse -> {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            if (apiResponse.getData() != null) {
                townshipVOList = apiResponse.getData();
                setUpTownShip();
            } else {
                if (apiResponse.getError() instanceof ApiException) {
                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                    if (errorCode == 401) {
                        super.refreshAccessToken();
                    } else if (errorCode == 204) {
                        // TODO: Server response successful but there is no data (Empty response).
                    } else if (errorCode == 200) {
                        // TODO: Reach End of List
                        Snackbar.make(btnOk, "End of Product List", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    retryDialog.show();
                    retryDialog.tvRetry.setText("Network error!");
                    retryDialog.btnRetry.setOnClickListener(v -> {
                        retryDialog.dismiss();
                        getRegion();
                    });
                }
            }
        });
    }

    private void setUpRegion() {
        ArrayAdapter<RegionVO> regionAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, regionVOList);
        // Specify the layout to use when the list of choices appears
        regionAdapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spDivision.setAdapter(regionAdapter);
        for(int i =0;i<regionVOList.size();i++){
            if(regionVOList.get(i).getDescEN().equals(customerVO.getDivision())){
                spDivision.setSelection(i);
            }
        }
        spDivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                getTownShip(regionVOList.get(position).getCode());
                selectedRegion=regionVOList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setUpTownShip() {
        ArrayAdapter<TownshipVO> regionAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, townshipVOList);
        // Specify the layout to use when the list of choices appears
        regionAdapter.setDropDownViewResource(R.layout.spinner_item);
        // Apply the adapter to the spinner
        spTownship.setAdapter(regionAdapter);
        for(int i =0;i<townshipVOList.size();i++){
            if(townshipVOList.get(i).getDescEN().equals(customerVO.getDivision())){
                spTownship.setSelection(i);
            }
        }
        spTownship.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTownship=townshipVOList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnOk:
                sendEditUser();
                Toast.makeText(this, "Region: "+selectedRegion +", Township: "+selectedTownship, Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnCancel:
                super.onBackPressed();
                break;
        }
    }

    private void sendEditUser() {
        CustomerCriteria criteria = new CustomerCriteria();
        criteria.setName(String.valueOf(etName.getText()));
        criteria.setAddress(String.valueOf(etAddress.getText()));
        criteria.setPhone(String.valueOf(etPhone.getText()));
        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Update Customer Information.Please wait!");
        loadingDialog.show();
        editViewModel.updateCustomer(criteria).observe(this, apiResponse -> {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            if (apiResponse.getData() != null) {
                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
                super.onBackPressed();
            } else {
                if (apiResponse.getError() instanceof ApiException) {
                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                    if (errorCode == 401) {
                        super.refreshAccessToken();
                    } else if (errorCode == 204) {
                        // TODO: Server response successful but there is no data (Empty response).
                    } else if (errorCode == 200) {
                        // TODO: Reach End of List
                        Snackbar.make(btnOk, "End of Product List", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    retryDialog.show();
                    retryDialog.tvRetry.setText("Network error!");
                    retryDialog.btnRetry.setOnClickListener(v -> {
                        retryDialog.dismiss();
                        getRegion();
                    });
                }
            }
        });
    }
}
