package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.OrderHistoryAdapter;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.datas.vos.OrderHistoryResponse;
import com.creative_webstudio.iba.datas.vos.OrderHistoryVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.OrderHistoryViewModel;
import com.google.gson.Gson;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderHistoryActivity extends BaseActivity {
    @BindView(R.id.recycler_order_history)
    SmartRecyclerView rvOrderHistory;

    private List<OrderHistoryVO> orderList;
    OrderHistoryAdapter mAdapter;
    int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this,this);
        orderList = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order History");

        mAdapter = new OrderHistoryAdapter(this);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvOrderHistory.setAdapter(mAdapter);

        getOrderHistory(++mCurrentPage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getOrderHistory(int page) {
        OrderHistoryViewModel viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel.class);
        viewModel.getOrderHistory(page).observe(this, apiResponse -> {
            if (apiResponse.getData() != null) {
                orderList=apiResponse.getData().getOrderHistoryList();
                mAdapter.appendNewData(orderList);
            } else {
                if (apiResponse.getError() instanceof ApiException) {
                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                    if (errorCode == 401) {
                        super.refreshAccessToken();
                    } else if (errorCode == 204) {
                        // TODO: Server response successful but there is no data (Empty response).
                    } else if (errorCode == 200) {
                        // TODO: Reach End of List
                        Snackbar.make(rvOrderHistory, "End of Product List", Snackbar.LENGTH_LONG).show();
                    }
                } else {
                    // TODO: Network related error occurs. Show the retry button with status text so that user can retry.
                }
            }
        });
    }

    public void onItemClick(OrderHistoryVO orderHistoryVO){
        Gson gson = new Gson();
        String json = gson.toJson(orderHistoryVO);
        startActivity(OrderItemsActivity.newIntent(this,json));
    }
}
