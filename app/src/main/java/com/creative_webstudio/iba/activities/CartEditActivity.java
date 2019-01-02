package com.creative_webstudio.iba.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.CartAdapter;
import com.creative_webstudio.iba.adapters.CartEditAdapter;
import com.creative_webstudio.iba.components.SmartRecyclerView;
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

    List<CartVO> orderCartList;
    List<CartVO> cartEditList;
    List<ProductVO> productVOList;
    List<CartShowVO> cartShowVOList;
    CartEditAdapter mAdapter;

    private IBAPreferenceManager ibaShared;

    public static Intent newIntent(Context context, String orderItems,String products) {
        Intent intent = new Intent(context, CartEditActivity.class);
        intent.putExtra("OrderItems",orderItems);
        intent.putExtra("Product",products);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_edit);
        ButterKnife.bind(this,this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        cartEditList = new ArrayList<>();
        productVOList = new ArrayList<>();
        orderCartList = new ArrayList<>();
        mAdapter = new CartEditAdapter(this);
        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCart.setAdapter(mAdapter);
        ibaShared = new IBAPreferenceManager(this);
        Gson gson = new Gson();
        if(getIntent().hasExtra("OrderItems")){
            TypeToken<List<CartVO>> token = new TypeToken<List<CartVO>>() {};
            cartEditList = gson.fromJson(getIntent().getStringExtra("OrderItems"),token.getType());
        }
        if(getIntent().hasExtra("Product")){
            TypeToken<List<ProductVO>> token = new TypeToken<List<ProductVO>>() {};
            productVOList = gson.fromJson(getIntent().getStringExtra("Product"),token.getType());
        }
        setUpData();

    }

    private void setUpRecycler() {
        cartShowVOList = new ArrayList<>();
        ProductVO tempProduct = new ProductVO();
        OrderUnitVO tempOrder = new OrderUnitVO();
        for (int i = 0; i < cartEditList.size(); i++) {
            for (ProductVO productVO : productVOList) {
                if (productVO.getId() == cartEditList.get(i).getProductId()) {
                    tempProduct = productVO;
                }
            }
            if (tempProduct.getOrderUnits().size() > 0) {
                for (OrderUnitVO order : tempProduct.getOrderUnits()) {
                    if (order.getId() == cartEditList.get(i).getOrderUnitId()) {
                        tempOrder = order;
                    }
                }
            }
            CartShowVO cartShowVO = new CartShowVO();
            cartShowVO.setProductName(tempProduct.getProductName());
            if (!tempProduct.getThumbnailIdsList().isEmpty()) {
                cartShowVO.setThumbnailId(tempProduct.getThumbnailIdsList().get(0));
            }
//            for(CartVO cart:orderCartList){
//                if(cart.getProductId()==tempProduct.getId() && cart.getOrderUnitId()==tempOrder.getId()){
//                    cartShowVO.setItemQuantity(cart.getQuantity());
//                }
//            }
            cartShowVO.setItemQuantity(10);
            cartShowVO.setMin(tempOrder.getMinimumOrderQuantity());
//            cartShowVO.setMax(tempOrder.getUnitInStock());
            cartShowVO.setMax(20);
            cartShowVO.setUnitId(tempOrder.getId());
            cartShowVO.setProductId(tempProduct.getId());
            cartShowVOList.add(cartShowVO);
            mAdapter.setNewData(cartShowVOList);
        }
    }

    public void updateOrder(int pos,int quantity,CartShowVO cartShowVO){
        if(quantity>0){
            cartShowVOList.get(pos).setItemQuantity(quantity);
        }else {
            onRemoveCart(cartShowVO);
        }
    }

    public void onRemoveCart(CartShowVO cart) {
        CartVO cartVO = new CartVO();
        cartVO.setProductId(cart.getProductId());
        cartVO.setOrderUnitId(cart.getUnitId());
        cartVO.setQuantity(cart.getItemQuantity());
        List<CartVO> tempList = new ArrayList<>();
        for(CartVO temp:cartEditList){
            if(temp.getProductId()!=cartVO.getProductId() || temp.getOrderUnitId()!=cartVO.getOrderUnitId()){
                tempList.add(temp);
            }
        }
        cartEditList=tempList;
//        if (ibaShared.removeCart(cartVO)) {
//            mAdapter.clearData();
//            Toast.makeText(this, "Removed!", Toast.LENGTH_SHORT).show();
//            setUpData(); // Fetch data from server.
//        }
        setUpData();
    }

    private void setUpData() {
        if (ibaShared.getItemsFromCart() != null) {
            orderCartList = ibaShared.getItemsFromCart();
        }
        setUpRecycler();
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
