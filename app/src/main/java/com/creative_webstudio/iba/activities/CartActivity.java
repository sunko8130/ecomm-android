package com.creative_webstudio.iba.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.adapters.CartAdapter;
import com.creative_webstudio.iba.components.EmptyViewPod;
import com.creative_webstudio.iba.components.SmartRecyclerView;
import com.creative_webstudio.iba.datas.criterias.PromoRewardDetailCriteria;
import com.creative_webstudio.iba.datas.vos.CartShowVO;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.OrderUnitVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.PromoRewardDetailVO;
import com.creative_webstudio.iba.datas.vos.PromoRewardVO;
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
    SmartRecyclerView rvCart;

    @BindView(R.id.btnOrder)
    Button btnOrder;

    @BindView(R.id.tvCartCount)
    MMTextView tvCartCount;

    @BindView(R.id.tvSubTotal)
    MMTextView tvSubtotal;

//    @BindView(R.id.tvTax)
//    MMTextView tvTax;

    @BindView(R.id.tvTotal)
    MMTextView tvTotal;

    //empty view
    @BindView(R.id.vp_empty_product)
    EmptyViewPod emptyViewPod;

    @Nullable
    @BindView(R.id.tv_empty)
    TextView tvEmpty;

    @Nullable
    @BindView(R.id.btn_refresh_empty)
    MMTextView btnRefresh;

    @Nullable
    @BindView(R.id.anim_empty)
    LottieAnimationView animEmpty;

    @Nullable
    @BindView(R.id.iv_empty)
    ImageView ivEmpty;

    @BindView(R.id.layoutOne)
    LinearLayout layoutOne;

    @BindView(R.id.layoutTwo)
    LinearLayout layoutTwo;

    private CartAdapter mCartAdapter;
    private IBAPreferenceManager ibaShared;
    private long productIds[];
    private CartViewModel cartViewModel;
    private List<CartShowVO> cartShowVOList;
    private List<ProductVO> productVOList;
    private List<CartVO> cartVOList;

    private Double total = 0.0;
    private int totalCartItem = 0;

    //loading dialog
    AlertDialog loadingDialog;

    private List<PromoRewardDetailVO> promoDetailList;

    private List<PromoRewardDetailCriteria> promoCriteriaList;

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
        promoDetailList = new ArrayList<>();
        mCartAdapter = new CartAdapter(this);
        rvCart.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvCart.setAdapter(mCartAdapter);
        rvCart.setEmptyView(emptyViewPod);
        ivEmpty.setVisibility(View.GONE);
        animEmpty.setVisibility(View.VISIBLE);
        animEmpty.setAnimation(R.raw.bag_error);
        animEmpty.playAnimation();
        tvEmpty.setText("There is no item in your cart!");
        btnRefresh.setVisibility(View.GONE);


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpData();
        btnOrder.setOnClickListener(this);
    }

    private void setUpData() {
        loadingDialog = CustomDialog.loadingDialog2(this, "Loading!", "Loading Your Order.Please wait!");
        total = 0.0;
        totalCartItem = 0;
        if (ibaShared.getItemsFromCart() != null && ibaShared.getItemsFromCart().size()!=0) {
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
                tvCartCount.setText(0 + " items in your cart.");
            }

        } else {
            mCartAdapter.clearData();
            tvCartCount.setText("0 Item in your cart.");
            animEmpty.playAnimation();
            productIds = null;
            cartVOList = new ArrayList<>();
            layoutOne.setVisibility(View.GONE);
            layoutTwo.setVisibility(View.GONE);
        }
    }

    private void getCartProducts(int page, long[] productIds) {
        animEmpty.setAnimation(R.raw.shopping_cart);
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
                    animEmpty.setAnimation(R.raw.bag_error);
                }
                if (apiResponse.getData() != null) {
                    productVOList = apiResponse.getData().getProductVOList();
                    getPromoCriteria();
//                    setUpRecycler();
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
                                retryDialog.dismiss();
                                getCartProducts(page, productIds);
                            });
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

    private void getPromoCriteria() {
        promoCriteriaList = new ArrayList<>();
        ProductVO tempProduct;
        for (int i = 0; i < cartVOList.size(); i++) {
            for (ProductVO productVO : productVOList) {
                if (productVO.getId().equals(cartVOList.get(i).getProductId())) {
                    tempProduct = productVO;
                    if (tempProduct.getOrderUnits().size() > 0) {
                        for (OrderUnitVO order : tempProduct.getOrderUnits()) {
                            if (order.getId().equals(cartVOList.get(i).getOrderUnitId())) {
//                                if (order.getHasPromotion() || order.getHasPromotion()!=null) {
                                for (PromoRewardVO promoRewardVO : order.getPromoRewardVOList()) {
                                    PromoRewardDetailCriteria promo = new PromoRewardDetailCriteria();
                                    promo.setOrderUnitId(order.getId());
                                    promo.setPromoRewardId(promoRewardVO.getId());
                                    promoCriteriaList.add(promo);
                                }
//                                }
                            }
                        }
                    }
                }
            }
        }
        if (promoCriteriaList.size() > 0) {
            getPromoDetail(promoCriteriaList);
        } else {
            setUpRecycler();
        }
    }

    private void getPromoDetail(List<PromoRewardDetailCriteria> criteria) {
        if (!checkNetwork()) {
            retryDialog.show();
            retryDialog.tvRetry.setText("No Internet Connection");
            retryDialog.btnRetry.setOnClickListener(v -> {
                retryDialog.dismiss();
                getPromoDetail(criteria);
            });
        } else {
            loadingDialog.show();
            cartViewModel.getPromoDetail(criteria).observe(this, apiResponse -> {
                if (loadingDialog.isShowing()) {
                    loadingDialog.dismiss();
                }
                if (apiResponse.getData() != null) {
                    promoDetailList = apiResponse.getData();
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
                        } else {
                            retryDialog.show();
                            retryDialog.tvRetry.setText("No Internet Connection");
                            retryDialog.btnRetry.setOnClickListener(v -> {
                                retryDialog.dismiss();
                                getPromoDetail(promoCriteriaList);
                            });
                        }
                    }
                }
            });
        }
    }

    private void setUpRecycler() {
        Double tempTotal;
        cartShowVOList = new ArrayList<>();
        ProductVO tempProduct = new ProductVO();
        OrderUnitVO tempOrder = new OrderUnitVO();
        Double promoAmount = 0.0;
        String promoItem = "";
        for (int i = 0; i < cartVOList.size(); i++) {
            for (ProductVO productVO : productVOList) {
                if (productVO.getId().equals(cartVOList.get(i).getProductId())) {
                    tempProduct = productVO;
                }
            }
            if (tempProduct.getOrderUnits().size() > 0) {
                for (OrderUnitVO order : tempProduct.getOrderUnits()) {
                    if (order.getId().equals(cartVOList.get(i).getOrderUnitId())) {
                        tempOrder = order;
                    }
                }
            }
            for (PromoRewardDetailVO promoDetail : promoDetailList) {
                if (tempOrder.getId().equals(promoDetail.getOrderUnitId())) {
                    for (PromoRewardVO promoRewardVO : tempOrder.getPromoRewardVOList()) {
                        if (promoRewardVO.getId().equals(promoDetail.getPromoRewardId()) && promoDetail.getPromoRewardId().equals(promoRewardVO.getId())) {
                            promoRewardVO.setPromoQuantity(promoDetail.getOrderQuantity());
                        }
                    }
                }
            }
            CartShowVO cartShowVO = new CartShowVO();
            cartShowVO.setProductName(tempProduct.getProductName());
            cartShowVO.setItemQuantity(cartVOList.get(i).getQuantity());
            if (tempProduct.getThumbnailIdsList() != null && !tempProduct.getThumbnailIdsList().isEmpty()) {
                cartShowVO.setThumbnailId(tempProduct.getThumbnailIdsList().get(0));
            }
            if (tempProduct.getHasPromotion()) {
                promoAmount = 0.0;
                promoItem = "";
                for (PromoRewardVO promoRewardVO : tempOrder.getPromoRewardVOList()) {
                    if (cartVOList.get(i).getQuantity() >= promoRewardVO.getPromoQuantity()) {
                        if (promoRewardVO.getRewardType().equals("Give Away")) {
                            promoItem = promoItem.concat("Get " + promoRewardVO.getRewardName() + " ");
                        }
                        if (promoRewardVO.getRewardType().equals("Percentage")) {
                            promoAmount += cartVOList.get(i).getQuantity() * tempOrder.getPricePerUnit() * (promoRewardVO.getDiscountValue() / 100);
                            promoItem = promoItem.concat("(" + promoRewardVO.getDiscountValue() + "% Off)");
                        }
                        if (promoRewardVO.getRewardType().equals("Fixed Amount")) {
                            int temp = cartVOList.get(i).getQuantity() / promoRewardVO.getPromoQuantity();
                            promoAmount += promoRewardVO.getDiscountValue() * temp;
                            promoItem = promoItem.concat("(" + promoRewardVO.getDiscountValue() + " Ks Save)\n");
                        }

                    }
                }
//                String str = String.format("%,.2f", (tempOrder.getPricePerUnit() * cartVOList.get(i).getQuantity()));
                cartShowVO.setPromoItem(promoItem);
                if (promoAmount > 0) {
                    cartShowVO.setPromoAmount(tempOrder.getPricePerUnit() * cartVOList.get(i).getQuantity());
                    cartShowVO.setPricePerUnit(cartVOList.get(i).getQuantity() * tempOrder.getPricePerUnit() - promoAmount);
                    tempTotal = cartVOList.get(i).getQuantity() * tempOrder.getPricePerUnit() - promoAmount;
                } else {
                    cartShowVO.setPricePerUnit(tempOrder.getPricePerUnit() * cartVOList.get(i).getQuantity());
                    tempTotal = cartVOList.get(i).getQuantity() * tempOrder.getPricePerUnit();
                }
            } else {
                cartShowVO.setPricePerUnit(tempOrder.getPricePerUnit() * cartVOList.get(i).getQuantity());
                tempTotal = cartVOList.get(i).getQuantity() * tempOrder.getPricePerUnit();
            }
            cartShowVO.setUnitShow("- ( 1" + tempOrder.getUnitName() + " per " + tempOrder.getItemsPerUnit() + " " + tempOrder.getItemName() + ")");
            cartShowVO.setUnitId(tempOrder.getId());
            cartShowVO.setProductId(tempProduct.getId());
            cartShowVOList.add(cartShowVO);
            total += tempTotal;
            totalCartItem += cartVOList.get(i).getQuantity();

        }
        mCartAdapter.setNewData(cartShowVOList);
        String s = String.format("%,.2f", total);
        tvTotal.setText(s + " Ks");
        tvSubtotal.setText(s + " Ks");
        tvCartCount.setText(String.valueOf(totalCartItem) + " Items in your cart.");
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
        if (!loadingDialog.isShowing()) {
            super.onBackPressed();
        }
    }

    public void onRemoveCart(CartShowVO cart) {
        Bundle bundle = new Bundle();
        bundle.putLong("product_id", cart.getProductId());
        bundle.putString("product_name", cart.getProductName());
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "remove_from_cart");
        mFirebaseAnalytics.logEvent("click_cart_remove", bundle);
        CartVO cartVO = new CartVO();
        cartVO.setProductId(cart.getProductId());
        cartVO.setOrderUnitId(cart.getUnitId());
        cartVO.setQuantity(cart.getItemQuantity());
        if (ibaShared.removeCart(cartVO)) {
            mCartAdapter.clearData();
            setUpData(); // Fetch data from server.
        }
    }

    private void DeleteItem(int deleteItem) {
        CartShowVO tempCartShow = cartShowVOList.get(deleteItem);
        List<CartVO> tempList = new ArrayList<>();
        for (CartVO cartVO : cartVOList) {
            if (cartVO.getProductId().equals(tempCartShow.getProductId()) && cartVO.getOrderUnitId().equals(tempCartShow.getUnitId())) {
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
        // click_order
        // order_id -> 123456

        Bundle bundle = new Bundle();
        for (int i = 0; i < cartVOList.size(); i++) {
            bundle.putString(String.valueOf(cartShowVOList.get(i).getProductId()), cartShowVOList.get(i).getProductName() + "-" + cartShowVOList.get(i).getUnitShow() + "-" + cartVOList.get(i).getQuantity());
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

//    @Override
//    public void onAccessTokenRefreshSuccess(Response<TokenVO> response) {
//        getCartProducts(0, productIds);
//    }
}
