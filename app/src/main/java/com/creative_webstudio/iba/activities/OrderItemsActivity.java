package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.widget.Button;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.CartAdapter;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.datas.vos.CartShowVO;
import com.creative_webstudio.iba.datas.vos.OrderHistoryVO;
import com.creative_webstudio.iba.datas.vos.OrderItemVO;
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

public class OrderItemsActivity extends BaseActivity {
    @BindView(R.id.recycler_order_item)
    SmartRecyclerView rvOrderItem;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    private OrderHistoryVO orderHistoryVO;
    private List<OrderItemVO> itemList;
    private long[] itemIds;

    private CartAdapter mAdapter;

    private boolean canUpdate = true;

    AlertDialog loadingDialog;

    public static Intent newIntent(Context context, String itemVo) {
        Intent i = new Intent(context, OrderItemsActivity.class);
        i.putExtra("OrderVO", itemVo);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);
        ButterKnife.bind(this, this);
        itemList = new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order Items");

        mAdapter = new CartAdapter(this);
        rvOrderItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvOrderItem.setAdapter(mAdapter);
        if (getIntent().hasExtra("OrderVO")) {
            String json = getIntent().getStringExtra("OrderVO");
            Gson gson = new Gson();
            orderHistoryVO = gson.fromJson(json, OrderHistoryVO.class);
        }

        if (!orderHistoryVO.getStatus().equals("Pending")) {
            btnCancel.setText(orderHistoryVO.getStatus());
            if (!orderHistoryVO.getStatus().equals("Customer Canceled")) {
                btnCancel.setTextColor(ContextCompat.getColor(this, R.color.limeGreen));
            } else {
                btnCancel.setTextColor(ContextCompat.getColor(this, R.color.redFull));
            }
            disableCancel();
        } else {
            canUpdate = true;
            btnCancel.setText("Cancel Order");
            btnCancel.setTextColor(ContextCompat.getColor(this, R.color.whiteFull));
            btnCancel.setBackground(getDrawable(R.drawable.round_rect_black_line_cancel));
            btnCancel.setOnClickListener(view -> {
                final AlertDialog.Builder builder = new AlertDialog.Builder(OrderItemsActivity.this);
                builder.setTitle("Are Your Sure!");
                builder.setMessage("Do You want to cancel this order?");
                builder.setPositiveButton("Ok", (dialog, which) -> cancelOrder(orderHistoryVO.getId()));
                builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                AlertDialog productDialog = builder.create();
                productDialog.show();
            });
        }
        itemList = orderHistoryVO.getOrderItems();
        itemIds = new long[itemList.size()];
        for (int i = 0; i < itemList.size(); i++) {
            itemIds[i] = itemList.get(i).getId();
        }
        getOrderItems(itemIds);

    }

    private void cancelOrder(long itemId) {
        Bundle bundle = new Bundle();
        bundle.putLong("order_id", orderHistoryVO.getId());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "cancel_order");
        mFirebaseAnalytics.logEvent("click_cancel_order", bundle);
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                cancelOrder(itemId);
            });
        } else {
            loadingDialog = CustomDialog.loadingDialog2(this, "Canceling!", "Canceling Your Order!.Please wait!");
            loadingDialog.show();
            OrderHistoryViewModel viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel.class);
            viewModel.cancelOrder(itemId).observe(this, apiResponse -> {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (apiResponse.getData() != null) {
                    canUpdate = false;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(OrderItemsActivity.this);
                    builder.setTitle("Success");
                    builder.setMessage("Your order update is success!");
                    builder.setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                        disableCancel();
                    });
                    AlertDialog productDialog = builder.create();
                    productDialog.show();
                    btnCancel.setText("Customer Canceled");
                    btnCancel.setTextColor(ContextCompat.getColor(this, R.color.redFull));
                    btnCancel.setBackground(getDrawable(R.drawable.round_rect_black_line));
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(rvOrderItem, "End of Product List", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("No Internet Connection");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            cancelOrder(itemId);
                        });
                    }
                }
            });
        }
    }

    private void disableCancel() {
        btnCancel.setOnClickListener(view -> {
            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderItemsActivity.this);
            builder.setTitle("Denied!");
            builder.setMessage("You can't update this order!");
            builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
            AlertDialog productDialog = builder.create();
            productDialog.show();
        });
    }

    private void getOrderItems(long[] itemIds) {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getOrderItems(itemIds);
            });
        } else {
            loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Order Items.Please wait!");
            loadingDialog.show();
            OrderHistoryViewModel viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel.class);
            viewModel.getOrderItems(itemIds).observe(this, apiResponse -> {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (apiResponse.getData() != null) {
                    itemList = apiResponse.getData();
                    setUpAdapter(itemList);
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(rvOrderItem, "End of Product List", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("No Internet Connection");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            getOrderItems(itemIds);
                        });
                    }
                }
            });
        }
    }

    private void setUpAdapter(List<OrderItemVO> itemList) {
        List<CartShowVO> cartShowVOList = new ArrayList<>();
        for (OrderItemVO order : itemList) {
            CartShowVO cartShowVO = new CartShowVO();
            cartShowVO.setProductName(order.getProduct().getProductName());
            cartShowVO.setItemQuantity(order.getQuantity());
            if (order.getProduct().getThumbnailIdsList().size() > 0) {
                cartShowVO.setThumbnailId(order.getProduct().getThumbnailIdsList().get(0));
            }
            cartShowVO.setUnitShow("- ( 1" + order.getOrderUnit().getUnitName() + " per " + order.getOrderUnit().getItemsPerUnit() + " " + order.getOrderUnit().getItemName() + ")");
            cartShowVO.setPricePerUnit(order.getOrderUnit().getPricePerUnit());
            cartShowVOList.add(cartShowVO);
        }
        mAdapter.setNewData(cartShowVOList);
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
    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {
        getOrderItems(itemIds);
    }
}
