package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.MenuItem;
import android.view.View;
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

import org.mmtextview.components.MMTextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class OrderItemsActivity extends BaseActivity {
    @BindView(R.id.recycler_order_item)
    SmartRecyclerView rvOrderItem;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    @BindView(R.id.tvStatus)
    MMTextView tvStatus;

    @BindView(R.id.tvInfo)
    MMTextView tvInfo;

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
        tvStatus.setText(orderHistoryVO.getStatus());
        switch (orderHistoryVO.getStatus()) {
            case "Pending":
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.blue2));
                break;
            case "Shipped":
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.orange));
                break;
            case "Completed":
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.limeGreen));
                break;
            default:
                tvStatus.setTextColor(ContextCompat.getColor(this, R.color.redFull));
                break;
        }
        if (orderHistoryVO.getStatus().equals("Pending") || orderHistoryVO.getStatus().equals("Shipped")) {
            long timeInMilliseconds = 0;
            String givenDateString = orderHistoryVO.getOrderDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            try {
                Date mDate = sdf.parse(givenDateString);
//                String s = sdf.format(System.currentTimeMillis() + (1000 * 60 * 60 * 24));
                timeInMilliseconds = mDate.getTime() + (1000 * 60 * 60 * 24);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (timeInMilliseconds < System.currentTimeMillis()) {
                disableCancel();
            } else {
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
            tvInfo.setVisibility(View.VISIBLE);

        } else {
            disableCancel();
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
                    tvStatus.setText("Customer Canceled");
                    tvStatus.setTextColor(ContextCompat.getColor(this, R.color.redFull));
                    tvInfo.setVisibility(View.GONE);
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
                        } else if (errorCode == 451) {
                            canUpdate = false;
                            final AlertDialog.Builder builder = new AlertDialog.Builder(OrderItemsActivity.this);
                            builder.setTitle("Access Denied");
                            builder.setMessage("You can't update this order now!");
                            builder.setPositiveButton("Ok", (dialog, which) -> {
                                dialog.dismiss();
                                disableCancel();
                            });
                            AlertDialog productDialog = builder.create();
                            productDialog.show();
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
        btnCancel.setVisibility(View.GONE);
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
            if (order.getProduct().getThumbnailIdsList() != null && order.getProduct().getThumbnailIdsList().size() > 0) {
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
    public void onBackPressed() {
        if (!loadingDialog.isShowing()) {
            super.onBackPressed();
        }
    }

    @Override
    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {
        getOrderItems(itemIds);
    }
}
