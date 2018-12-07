package com.creative_webstudio.iba.mvp.views;

public interface ProductView extends BaseView{
    void showProductDetail(Double infoId);
    void goProductSearchScreen();
    void onTapShoppingCart();
    void showTokenError(Integer errorCode);
}
