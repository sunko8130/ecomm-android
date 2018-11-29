package com.creative_webstudio.iba.mvp.presenters;

import com.creative_webstudio.iba.delegates.ProductSearchDelegate;
import com.creative_webstudio.iba.mvp.views.ProductSearchView;

public class ProductSearchPresenter extends BasePresenter<ProductSearchView> implements ProductSearchDelegate {
    @Override
    public void onTapView() {
        mView.onTapView();
    }
}
