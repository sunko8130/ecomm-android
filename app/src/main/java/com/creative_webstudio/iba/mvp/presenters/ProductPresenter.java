package com.creative_webstudio.iba.mvp.presenters;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.CriteriaVo;
import com.creative_webstudio.iba.datas.vos.HCInfoVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.delegates.ProductDelegate;
import com.creative_webstudio.iba.mvp.views.ProductView;

import java.util.List;

public class ProductPresenter extends BasePresenter<ProductView> implements ProductDelegate {

    private MutableLiveData<List<HCInfoVO>> mInfoList;

    @Override
    public void initPresenter(ProductView mView) {
        super.initPresenter(mView);
        mInfoList = new MutableLiveData<>();
        forceRefresh();
    }

    public void forceRefresh() {
        IbaModel.getInstance().loadHCInfo(mInfoList, mErrorLD);
    }

    public void setErrorNull() {
        mErrorLD = new MutableLiveData<>();
    }

    public LiveData<List<HCInfoVO>> getInfoList() {
        return mInfoList;
    }


    @Override
    public void onTapView(Double infoId) {
        mView.showProductDetail(infoId);
//        startActivity(ProductDetailsActivity.newIntent(this,"Product"));
//        overridePendingTransition(R.anim.rotate_clockwise_anim, R.anim.zoom_out_anim);
    }

    @Override
    public void onTapSearch() {
        Log.e("onTapSearch", "onTapSearch: ");
        MutableLiveData<List<ProductVO>> productSearchActivityMutableLiveData = new MutableLiveData<>();
        CriteriaVo criteriaVo = new CriteriaVo(0, 10);
        //IbaModel.getInstance().getProductSearchList(criteriaVo, productSearchActivityMutableLiveData, mResponseCode);
        IbaModel.getInstance().getProductPaging(criteriaVo);
        mView.onTapSearch();
//        Intent i = new Intent(this, ProductSearchActivity.class);
//        startActivity(ProductSearchActivity.newIntent(this));
    }

    @Override
    public void onTapShoppingCart() {
        mView.onTapShoppingCart();
    }
}
