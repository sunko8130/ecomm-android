package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.OrderHistoryAdapter;
import com.creative_webstudio.iba.components.CustomRetryDialog;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.datas.vos.OrderHistoryVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.OrderHistoryViewModel;
import com.creative_webstudio.iba.utils.CustomDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;



import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class OrderHistoryActivity extends BaseActivity {
    @BindView(R.id.recycler_order_history)
    SmartRecyclerView rvOrderHistory;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private List<OrderHistoryVO> orderList;
    OrderHistoryAdapter mAdapter;
    //    int mCurrentPage;
    AlertDialog loadingDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this, this);
        orderList = new ArrayList<>();

        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Order History.Please wait!");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order History");

        mAdapter = new OrderHistoryAdapter(this);
        rvOrderHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvOrderHistory.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            mAdapter.clearData();
            getOrderHistory();
        });

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

    @Override
    protected void onResume() {
        super.onResume();
        mAdapter.clearData();
        getOrderHistory();
    }

    private void getOrderHistory() {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getOrderHistory();
            });
        }else {
            loadingDialog.show();
            OrderHistoryViewModel viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel.class);
            viewModel.getOrderHistory().observe(this, apiResponse -> {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                swipeRefreshLayout.setRefreshing(false);
                if (apiResponse.getData() != null) {
                    orderList = apiResponse.getData().getOrderHistoryList();
                    //change asc desc
//                    List<OrderHistoryVO> temp = new ArrayList<>();
//                    for(int i=orderList.size();i>0;i--){
//                        temp.add(orderList.get(i-1));
//                    }
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
                        retryDialog.show();
                        retryDialog.tvRetry.setText("No Internet Connection");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            getOrderHistory();
                        });
                    }
                }
            });
        }
    }

    public void onItemClick(OrderHistoryVO orderHistoryVO) {
        Bundle bundle = new Bundle();
        bundle.putLong("order_id", orderHistoryVO.getId());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "view_order_details");
        mFirebaseAnalytics.logEvent("click_order_item", bundle);
        Gson gson = new Gson();
        String json = gson.toJson(orderHistoryVO);
        startActivity(OrderItemsActivity.newIntent(this, json));
    }

    @Override
    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {
        getOrderHistory();
    }
}
