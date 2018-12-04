package com.creative_webstudio.iba.mvp.presenters;

import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.HCInfoVO;
import com.creative_webstudio.iba.mvp.views.ProductDetailView;

public class ProductDetailsPresenter extends BasePresenter<ProductDetailView> {
    @Override
    public void initPresenter(ProductDetailView mView) {
        super.initPresenter(mView);
    }

    public void getInfoById(double infoId){
        mView.showInfoById(IbaModel.getInstance().getInfoById(infoId));
    }
}
