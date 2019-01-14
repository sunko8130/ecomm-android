package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.CartAdapter;
import com.creative_webstudio.iba.datas.vos.CartShowVO;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.OrderUnitVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.viewmodels.CartViewModel;
import com.creative_webstudio.iba.utils.CustomDialog;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;

import org.mmtextview.components.MMProgressDialog;
import org.mmtextview.components.MMTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class CartActivity extends BaseActivity implements View.OnClickListener {
    @Nullable
    @BindView(R.id.rv_cart_list)
    RecyclerView rvCart;

    @BindView(R.id.btnOrder)
    Button btnOrder;

    @BindView(R.id.tvCartCount)
    MMTextView tvCartCount;

    @BindView(R.id.tvSubTotal)
    MMTextView tvSubtotal;

    @BindView(R.id.tvTax)
    MMTextView tvTax;

    @BindView(R.id.tvTotal)
    MMTextView tvTotal;

    private CartAdapter mCartAdapter;
    private IBAPreferenceManager ibaShared;
    private long productIds[];
    private CartViewModel cartViewModel;
    private List<CartShowVO> cartShowVOList;
    private List<ProductVO> productVOList;
    private List<CartVO> cartVOList;

    private long total = 0;
    private int totalCartItem = 0;

    //loading dialog
    AlertDialog loadingDialog;

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, CartActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        ButterKnife.bind(this, this);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        ibaShared = new IBAPreferenceManager(this);
        cartViewModel = ViewModelProviders.of(this).get(CartViewModel.class);
        cartShowVOList = new ArrayList<>();
        productVOList = new ArrayList<>();
        mCartAdapter = new CartAdapter(this);
        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCart.setAdapter(mCartAdapter);
        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Your Order.Please wait!");
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpData();
        btnOrder.setOnClickListener(this);
    }

    private void setUpData() {
        total = 0;
        totalCartItem = 0;
        if (ibaShared.getItemsFromCart() != null) {
            cartVOList = ibaShared.getItemsFromCart();
            productIds = new long[cartVOList.size()];
            for (int i = 0; i < cartVOList.size(); i++) {
                productIds[i] = cartVOList.get(i).getProductId();
            }
            // TODO: I have removed below line.
//            if (cartEditList.size() > 1) {
            if (!cartVOList.isEmpty()) {
                getCartProducts(0, productIds);
            } else {
                tvTotal.setText(0 + " MMK");
                tvSubtotal.setText(0 + " MMK");
                tvCartCount.setText(0 + " Items in your cart.");
            }

        } else {
            productIds = null;
            cartVOList = new ArrayList<>();
        }
    }

    private void getCartProducts(int page, long[] productIds) {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getCartProducts(page, productIds);
            });
        } else {
            loadingDialog.show();
            cartViewModel.getProductByID(page, productIds).observe(this, apiResponse -> {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (apiResponse.getData() != null) {
                    productVOList = apiResponse.getData().getProductVOList();
                    setUpRecycler();
                } else {
                    if (apiResponse.getError() instanceof ApiException) {
                        int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                        if (errorCode == 401) {
                            super.refreshAccessToken();
                        } else if (errorCode == 204) {
                            // TODO: Server response successful but there is no data (Empty response).
                        } else if (errorCode == 200) {
                            // TODO: Reach End of List
                            Snackbar.make(rvCart, "End of Product List", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("Network error!");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            retryDialog.dismiss();
                            getCartProducts(page, productIds);
                        });
                    }
                }
            });
        }
    }

    private void setUpRecycler() {
        long tempTotal;
        cartShowVOList = new ArrayList<>();
        ProductVO tempProduct = new ProductVO();
        OrderUnitVO tempOrder = new OrderUnitVO();
        for (int i = 0; i < cartVOList.size(); i++) {
            for (ProductVO productVO : productVOList) {
                if (productVO.getId() == cartVOList.get(i).getProductId()) {
                    tempProduct = productVO;
                }
            }
            if (tempProduct.getOrderUnits().size() > 0) {
                for (OrderUnitVO order : tempProduct.getOrderUnits()) {
                    if (order.getId() == cartVOList.get(i).getOrderUnitId()) {
                        tempOrder = order;
                    }
                }
            }
            CartShowVO cartShowVO = new CartShowVO();
            cartShowVO.setProductName(tempProduct.getProductName());
            cartShowVO.setItemQuantity(cartVOList.get(i).getQuantity());
            if (!tempProduct.getThumbnailIdsList().isEmpty()) {
                cartShowVO.setThumbnailId(tempProduct.getThumbnailIdsList().get(0));
            }
            cartShowVO.setUnitShow("- ( 1" + tempOrder.getUnitName() + " per " + tempOrder.getItemsPerUnit() + " " + tempOrder.getItemName() + ")");
            cartShowVO.setPricePerUnit(tempOrder.getPricePerUnit());
            cartShowVO.setUnitId(tempOrder.getId());
            cartShowVO.setProductId(tempProduct.getId());
            cartShowVOList.add(cartShowVO);
            tempTotal = cartVOList.get(i).getQuantity() * tempOrder.getPricePerUnit();
            total += tempTotal;
            totalCartItem += cartVOList.get(i).getQuantity();
        }
        mCartAdapter.setNewData(cartShowVOList);
        tvTotal.setText(total + " MMK");
        tvSubtotal.setText(total + " MMK");
        tvCartCount.setText(totalCartItem + " Items in your cart.");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_menu, menu);
        return true;
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
        super.onBackPressed();
//        startActivity(ProductActivity.newIntent(this));
    }

    public void onClickItem(int btnNo, ProductVO productVO, int deleteItem) {
        if (btnNo == 1) {
            DeleteItem(deleteItem);
            Snackbar.make(rvCart, "Delete Cart", Snackbar.LENGTH_LONG).show();
        } else {
            Snackbar.make(rvCart, "Click To View", Snackbar.LENGTH_LONG).show();
        }
    }

    public void onRemoveCart(CartShowVO cart) {
        Bundle bundle = new Bundle();
        bundle.putLong("product_id",cart.getProductId());
        bundle.putString("product_name", cart.getProductName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "remove_from_cart");
        mFirebaseAnalytics.logEvent("click_cart_remove", bundle);
        CartVO cartVO = new CartVO();
        cartVO.setProductId(cart.getProductId());
        cartVO.setOrderUnitId(cart.getUnitId());
        cartVO.setQuantity(cart.getItemQuantity());
        if (ibaShared.removeCart(cartVO)) {
            mCartAdapter.clearData();
            Toast.makeText(this, "Removed!", Toast.LENGTH_SHORT).show();
            setUpData(); // Fetch data from server.
        }
    }

    private void DeleteItem(int deleteItem) {
        CartShowVO tempCartShow = cartShowVOList.get(deleteItem);
        List<CartVO> tempList = new ArrayList<>();
        for (CartVO cartVO : cartVOList) {
            if (cartVO.getProductId() == tempCartShow.getProductId() && cartVO.getOrderUnitId() == tempCartShow.getUnitId()) {
            } else {
                tempList.add(cartVO);
            }
        }
        cartVOList = tempList;
        ibaShared.AddListToCart(cartVOList);
        mCartAdapter.clearData();
        setUpData();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnOrder:
                final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                if (cartVOList.size() < 1) {
                    builder.setTitle("Denied!");
                    builder.setMessage("You have no item to order!");
                    builder.setPositiveButton("Ok", (dialog, which) -> {
                        dialog.dismiss();
                    });
                } else {
                    builder.setTitle("Are you sure?");
                    builder.setMessage("Do you want to order now?");
                    builder.setPositiveButton("Ok", (dialog, which) -> {
                        sendOrder();
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                }
                AlertDialog productDialog = builder.create();
                productDialog.setCanceledOnTouchOutside(false);
                productDialog.show();
                break;
        }
    }

    private void sendOrder() {
        Bundle bundle = new Bundle();
        for(int i=0;i<cartVOList.size();i++){
            bundle.putString("order_item_"+i,cartShowVOList.get(i).getProductName()+"-"+cartShowVOList.get(i).getUnitShow()+"-"+cartVOList.get(i).getQuantity());
        }
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "send_order");
        mFirebaseAnalytics.logEvent("click_send_order", bundle);
        MMProgressDialog loadingDialog = CustomDialog.loadingDialog(this, "Sending!", "Your Order is sending.Please wait!");
        loadingDialog.show();
        cartViewModel.sendOrder(cartVOList).observe(this, apiResponse -> {
            if (loadingDialog.isShowing()) {
                loadingDialog.dismiss();
            }
            if (apiResponse.getData() != null) {
                if (apiResponse.getData() instanceof Integer) {
                    Toast.makeText(this, "Your Order is Success", Toast.LENGTH_LONG).show();
                    cartVOList = new ArrayList<>();
                    ibaShared.AddListToCart(cartVOList);
                    startActivity(new Intent(this, OrderHistoryActivity.class));
                    finish();
                } else {
                    List<CartVO> errorCart = (List<CartVO>) apiResponse.getData();
                    Gson gson = new Gson();
                    String json = gson.toJson(errorCart);
                    String productJson = gson.toJson(productVOList);
                    //error order item;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                    builder.setTitle("Oops!");
                    builder.setMessage("Some order items in this order is out of stock.Please Edit your order.");
                    builder.setPositiveButton("Ok", (dialog, which) -> {
                        startActivity(CartEditActivity.newIntent(this, json, productJson));
                    });
                    builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
                    AlertDialog productDialog = builder.create();
                    productDialog.setCanceledOnTouchOutside(false);
                    productDialog.show();
                }
//                cartEditList = new ArrayList<>();
//                ibaShared.AddListToCart(cartEditList);
//                setUpData();
//                mCartAdapter.clearData();
            } else {
                if (apiResponse.getError() instanceof ApiException) {
                    int errorCode = ((ApiException) apiResponse.getError()).getErrorCode();
                    if (errorCode == 401) {
                        super.refreshAccessToken();
                    } else if (errorCode == 204) {
                        // TODO: Server response successful but there is no data (Empty response).
                    } else if (errorCode == 200) {
                        // TODO: Reach End of List
                        Snackbar.make(rvCart, "End of Product List", Snackbar.LENGTH_LONG).show();
                    } else {
                        retryDialog.show();
                        retryDialog.tvRetry.setText("Network error!");
                        retryDialog.btnRetry.setOnClickListener(v -> {
                            loadingDialog.dismiss();
                            sendOrder();
                        });
                    }
                } else {
                    retryDialog.show();
                    retryDialog.tvRetry.setText("Network error!");
                    retryDialog.btnRetry.setOnClickListener(v -> {
                        loadingDialog.dismiss();
                        sendOrder();
                    });
                }
            }

        });
    }

    @Override
    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {
        getCartProducts(0, productIds);
    }
}
