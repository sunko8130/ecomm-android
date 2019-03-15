package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.CartEditAdapter;
import com.creative_webstudio.iba.datas.vos.CartShowVO;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.OrderUnitVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CartEditActivity extends BaseActivity {

    @BindView(R.id.rv_cart)
    RecyclerView rvCart;

    @BindView(R.id.btnOk)
    Button btnOk;

    @BindView(R.id.btnCancel)
    Button btnCancel;

    List<CartVO> orderCartList;
    List<CartVO> cartEditList;
    List<ProductVO> productVOList;
    List<CartShowVO> cartShowVOList;
    List<CartVO> editedList;
    CartEditAdapter mAdapter;

    private IBAPreferenceManager ibaShared;

    public static Intent newIntent(Context context, String orderItems, String products) {
        Intent intent = new Intent(context, CartEditActivity.class);
        intent.putExtra("OrderItems", orderItems);
        intent.putExtra("Product", products);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_edit);
        ButterKnife.bind(this, this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        cartEditList = new ArrayList<>();
        productVOList = new ArrayList<>();
        orderCartList = new ArrayList<>();
        editedList = new ArrayList<>();
        mAdapter = new CartEditAdapter(this);
        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCart.setAdapter(mAdapter);
        ibaShared = new IBAPreferenceManager(this);
        if (ibaShared.getItemsFromCart() != null) {
            editedList = ibaShared.getItemsFromCart();
        }
        Gson gson = new Gson();
        if (getIntent().hasExtra("OrderItems")) {
            TypeToken<List<CartVO>> token = new TypeToken<List<CartVO>>() {
            };
            cartEditList = gson.fromJson(getIntent().getStringExtra("OrderItems"), token.getType());
        }
        if (getIntent().hasExtra("Product")) {
            TypeToken<List<ProductVO>> token = new TypeToken<List<ProductVO>>() {
            };
            productVOList = gson.fromJson(getIntent().getStringExtra("Product"), token.getType());
        }
        setUpData();
        btnOk.setOnClickListener(v -> {
            EditOrderOk();
        });
        btnCancel.setOnClickListener(v -> {
            super.onBackPressed();
        });
    }

    private void setUpData() {
        if (ibaShared.getItemsFromCart() != null) {
            orderCartList = ibaShared.getItemsFromCart();
        }
        setUpRecycler();
    }

    private void setUpRecycler() {
        cartShowVOList = new ArrayList<>();
        ProductVO tempProduct = new ProductVO();
        OrderUnitVO tempOrder = new OrderUnitVO();
        for (int i = 0; i < cartEditList.size(); i++) {
            for (ProductVO productVO : productVOList) {
                if (productVO.getId().equals(cartEditList.get(i).getProductId())) {
                    tempProduct = productVO;
                }
            }
            if (tempProduct.getOrderUnits().size() > 0) {
                for (OrderUnitVO order : tempProduct.getOrderUnits()) {
                    if (order.getId().equals(cartEditList.get(i).getOrderUnitId())) {
                        tempOrder = order;
                    }
                }
            }
            CartShowVO cartShowVO = new CartShowVO();
            cartShowVO.setProductName(tempProduct.getProductName());
            if (!tempProduct.getThumbnailIdsList().isEmpty()) {
                cartShowVO.setThumbnailId(tempProduct.getThumbnailIdsList().get(0));
            }
            for(CartVO cart:orderCartList){
                if(cart.getProductId().equals(tempProduct.getId()) && cart.getOrderUnitId().equals(tempOrder.getId())){
                    cartShowVO.setItemQuantity(cart.getQuantity());
                }
            }
            cartShowVO.setMin(tempOrder.getMinimumOrderQuantity());
            cartShowVO.setMax(tempOrder.getUnitInStock());
            cartShowVO.setUnitId(tempOrder.getId());
            cartShowVO.setProductId(tempProduct.getId());
            cartShowVOList.add(cartShowVO);
        }
        mAdapter.setNewData(cartShowVOList);
    }

    public void updateOrder(int pos, int quantity, CartShowVO cartShowVO) {
        if (quantity > 0) {
            cartShowVOList.get(pos).setItemQuantity(quantity);
            for (CartVO cart : editedList) {
                if (cart.getProductId().equals(cartShowVOList.get(pos).getProductId()) && cart.getOrderUnitId().equals(cartShowVOList.get(pos).getUnitId())) {
                    cart.setQuantity(quantity);
                }
            }
        } else {
            for (CartVO cart : editedList) {
                if (cart.getProductId().equals(cartShowVOList.get(pos).getProductId()) && cart.getOrderUnitId().equals(cartShowVOList.get(pos).getUnitId())) {
                    cart.setQuantity(-1);
                }
            }
            onRemoveCart(cartShowVO);
        }
    }

    public void onRemoveCart(CartShowVO cart) {
        CartVO cartVO = new CartVO();
        cartVO.setProductId(cart.getProductId());
        cartVO.setOrderUnitId(cart.getUnitId());
        cartVO.setQuantity(cart.getItemQuantity());
        ibaShared.removeCart(cartVO);
        List<CartVO> tempList = new ArrayList<>();
        for (CartVO temp : cartEditList) {
            if (!temp.getProductId().equals(cartVO.getProductId()) || !temp.getOrderUnitId().equals(cartVO.getOrderUnitId())) {
                tempList.add(temp);
            }
        }
        cartEditList = tempList;
        setUpData();
    }



    private void EditOrderOk() {
        List<CartVO> temp=new ArrayList<>();
        for (CartVO cartVO:editedList){
            if(cartVO.getQuantity()!=-1){
                temp.add(cartVO);
            }
        }
        ibaShared.AddListToCart(temp);
        super.onBackPressed();
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
