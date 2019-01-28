package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.OrderHistoryAdapter;
import com.creative_webstudio.iba.components.CustomRetryDialog;
import com.creative_webstudio.iba.components.EmptyViewPod;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.datas.vos.OrderHistoryVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.OrderHistoryViewModel;
import com.creative_webstudio.iba.utils.CustomDialog;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;


import org.mmtextview.components.MMTextView;

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

    //empty view
    @BindView(R.id.vp_empty_history)
    EmptyViewPod vpEmpty;

    @Nullable
    @BindView(R.id.tv_empty)
    MMTextView tvEmpty;

    @Nullable
    @BindView(R.id.btn_refresh_empty)
    MMTextView btnRefresh;

    @Nullable
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;

    @Nullable
    @BindView(R.id.anim_empty)
    LottieAnimationView animEmpty;

    //load more
    @Nullable
    @BindView(R.id.aniLoadMore)
    LottieAnimationView aniLoadMore;

    private int mCurrentPage = 1;
    private boolean mIsLoading;

    LinearLayoutManager layoutManager;

    //Scroll top
    boolean scrollTop = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        ButterKnife.bind(this, this);
        orderList = new ArrayList<>();

//        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Order History.Please wait!");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order History");

        rvOrderHistory.setEmptyView(vpEmpty);
        btnRefresh.setVisibility(View.GONE);
        tvEmpty.setText("Loading order history........");
        mAdapter = new OrderHistoryAdapter(this);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvOrderHistory.setLayoutManager(layoutManager);
        rvOrderHistory.setAdapter(mAdapter);
        swipeRefreshLayout.setOnRefreshListener(() -> refreshData());

        rvOrderHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (!mIsLoading && layoutManager.findLastVisibleItemPosition() == mAdapter.getItemCount() - 1) {
                    mIsLoading = true;
                    aniLoadMore.setVisibility(View.VISIBLE);// Prevent duplicate request while fetching from server
                    getOrderHistory(mCurrentPage);
                }
                if (layoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    scrollTop = true;
                } else {
                    scrollTop = false;
                }
            }
        });

    }

    public void refreshData() {
        tvEmpty.setText("Loading Data......");
        btnRefresh.setVisibility(View.GONE);

        mAdapter.clearData();
        mIsLoading = true;
        mCurrentPage = 1;
        getOrderHistory(mCurrentPage);
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
        refreshData();
    }

    private void getOrderHistory(int page) {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getOrderHistory(mCurrentPage);
            });
        } else {
            OrderHistoryViewModel viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel.class);
            viewModel.getOrderHistory(page).observe(this, apiResponse -> {
                if (swipeRefreshLayout.isRefreshing()) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                aniLoadMore.setVisibility(View.GONE);
                if (apiResponse.getData() != null) {
                    orderList = apiResponse.getData().getOrderHistoryList();
                    //change asc desc
//                    List<OrderHistoryVO> temp = new ArrayList<>();
//                    for(int i=orderList.size();i>0;i--){
//                        temp.add(orderList.get(i-1));
//                    }
                    mCurrentPage++;
                    mIsLoading = false;
                    ivEmpty.setVisibility(View.GONE);
                    mAdapter.appendNewData(orderList);
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            ivEmpty.setVisibility(View.VISIBLE);
                            tvEmpty.setText("There is no Order History.....");
                            animEmpty.setVisibility(View.GONE);
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            if (mCurrentPage > 1) {
                                Snackbar.make(rvOrderHistory, "End of Order History List", Snackbar.LENGTH_LONG).show();
                            } else {
                                tvEmpty.setText("No Data to Display!");
                                btnRefresh.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("No Internet Connection");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            getOrderHistory(mCurrentPage);
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
        getOrderHistory(mCurrentPage);
    }

    @Override
    public void onBackPressed() {
        if (!scrollTop) {
            layoutManager.smoothScrollToPosition(rvOrderHistory, null, 0);
        } else {
            super.onBackPressed();
        }
    }
}
