package com.creative_webstudio.iba.mvp.views;

public interface ProductView extends BaseView{
    void showProductDetail(Long infoId);
    void goProductSearchScreen();
    void showTokenError(Integer errorCode);

//    void showProductList();
//    void onProductItemClick();
//    void onLoadMoreData();
}
