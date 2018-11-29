package com.creative_webstudio.iba.mvp.presenters;


import android.content.Intent;

import com.creative_webstudio.iba.R;
import com.creative_webstudio.iba.activities.ProductDetailsActivity;
import com.creative_webstudio.iba.activities.ProductSearchActivity;
import com.creative_webstudio.iba.delegates.ProductDelegate;
import com.creative_webstudio.iba.mvp.views.ProductView;

public class ProductPresenter extends BasePresenter<ProductView> implements ProductDelegate{

    @Override
    public void initPresenter(ProductView mView) {
        super.initPresenter(mView);
    }

    @Override
    public void onTapView() {
        mView.onTapView();
//        startActivity(ProductDetailsActivity.newIntent(this,"Product"));
//        overridePendingTransition(R.anim.rotate_clockwise_anim, R.anim.zoom_out_anim);
    }

    @Override
    public void onTapSearch() {
        mView.onTapSearch();
//        Intent i = new Intent(this, ProductSearchActivity.class);
//        startActivity(ProductSearchActivity.newIntent(this));
    }

    @Override
    public void onTapShoppingCart() {
        mView.onTapShoppingCart();
    }
}
