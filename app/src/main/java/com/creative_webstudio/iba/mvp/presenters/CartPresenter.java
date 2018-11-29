package com.creative_webstudio.iba.mvp.presenters;

import com.creative_webstudio.iba.delegates.CartDelegate;
import com.creative_webstudio.iba.mvp.views.CartView;

public class CartPresenter extends BasePresenter<CartView> implements CartDelegate {
    @Override
    public void onTapView() {
        mView.onTapView();
    }
}
