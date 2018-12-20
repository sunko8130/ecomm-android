package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.CartAdapter;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.datas.vos.CartShowVO;
import com.creative_webstudio.iba.datas.vos.OrderHistoryVO;
import com.creative_webstudio.iba.datas.vos.OrderItemVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.OrderHistoryViewModel;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderItemsActivity extends BaseActivity {
    @BindView(R.id.recycler_order_item)
    SmartRecyclerView rvOrderItem;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    private OrderHistoryVO orderHistoryVO;
    private List<OrderItemVO> itemList;
    private long[] itemIds;

    private CartAdapter mAdapter;

    private boolean canUpdate =true;

    public static Intent newIntent(Context context,String itemVo) {
        Intent i = new Intent(context, OrderItemsActivity.class);
        i.putExtra("OrderVO",itemVo);
        return i;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_items);
        ButterKnife.bind(this,this);
        itemList=new ArrayList<>();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Order History");

        mAdapter = new CartAdapter(this);
        rvOrderItem.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvOrderItem.setAdapter(mAdapter);

        if(getIntent().hasExtra("OrderVO")){
            String json = getIntent().getStringExtra("OrderVO");
            Gson gson = new Gson();
            orderHistoryVO = gson.fromJson(json, OrderHistoryVO.class);
        }

        if(!orderHistoryVO.getStatus().equals("Pending")){
            canUpdate = false;
            btnCancel.setText(orderHistoryVO.getStatus());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnCancel.setTextColor(getColor(R.color.limeGreen));
            }
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(OrderItemsActivity.this);
                    builder.setTitle("Denied!");
                    builder.setMessage("You have no item to order!");
                    builder.setPositiveButton("Ok", (dialog, which) -> {dialog.dismiss(); });
                    AlertDialog productDialog = builder.create();
                    productDialog.show();
                }
            });
        }else {
            canUpdate = true;
            btnCancel.setText("Cancel");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                btnCancel.setTextColor(getColor(R.color.redFull));
            }
            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    cancelOrder(orderHistoryVO.getId());
                }
            });
        }
        itemList = orderHistoryVO.getOrderItems();
        itemIds = new long[itemList.size()];
        for(int i=0;i<itemList.size();i++){
            itemIds[i] = itemList.get(i).getId();
        }
        getOrderItems(itemIds);

    }

    private void cancelOrder(long itemId) {
        OrderHistoryViewModel viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel.class);
        viewModel.cancelOrder(itemId).observe(this,apiResponse->{
            if (apiResponse.getData() != null) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(OrderItemsActivity.this);
                builder.setTitle("Success");
                builder.setMessage("Your order update is success!");
                builder.setPositiveButton("Ok", (dialog, which) -> {dialog.dismiss(); });
                AlertDialog productDialog = builder.create();
                productDialog.show();
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
                    Snackbar.make(rvOrderItem,"Can't update your order.Try Again Soon!",Snackbar.LENGTH_LONG).show();
                    // TODO: Network related error occurs. Show the retry button with status text so that user can retry.
                }
            }
        });
    }

    private void getOrderItems(long[] itemIds) {
        OrderHistoryViewModel viewModel = ViewModelProviders.of(this).get(OrderHistoryViewModel.class);
        viewModel.getOrderItems(itemIds).observe(this,apiResponse->{
            if (apiResponse.getData() != null) {
                itemList=apiResponse.getData();
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
                    // TODO: Network related error occurs. Show the retry button with status text so that user can retry.
                }
            }
        });
    }

    private void setUpAdapter(List<OrderItemVO> itemList) {
        List<CartShowVO> cartShowVOList = new ArrayList<>();
        for (OrderItemVO order:itemList){
            CartShowVO cartShowVO=new CartShowVO();
            cartShowVO.setProductName(order.getProduct().getProductName());
            cartShowVO.setItemQuantity(order.getQuantity());
            cartShowVO.setThumbnailId(order.getProduct().getThumbnailIdsList().get(0));
            cartShowVO.setUnitShow("- ( 1"+order.getOrderUnit().getUnitName()+" per "+order.getOrderUnit().getItemsPerUnit()+" "+order.getOrderUnit().getItemName()+")");
            cartShowVO.setPricePerUnit(order.getOrderUnit().getPricePerUnit());
            cartShowVOList.add(cartShowVO);
        }
        mAdapter.setNewData(cartShowVOList);
    }
}
