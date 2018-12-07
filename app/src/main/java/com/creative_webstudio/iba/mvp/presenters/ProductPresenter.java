package com.creative_webstudio.iba.mvp.presenters;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.CriteriaVo;
import com.creative_webstudio.iba.datas.vos.HCInfoVO;
import com.creative_webstudio.iba.datas.vos.ProductVo;
import com.creative_webstudio.iba.delegates.ProductDelegate;
import com.creative_webstudio.iba.enents.TokenEvent;
import com.creative_webstudio.iba.mvp.views.ProductView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ProductPresenter extends BasePresenter<ProductView> implements ProductDelegate {

    private MutableLiveData<List<ProductVo>> mProductList;

    @Override
    public void initPresenter(ProductView mView) {
        super.initPresenter(mView);
        mProductList = new MutableLiveData<>();
        forceRefresh();
    }

    public void forceRefresh() {
        CriteriaVo criteriaVo = new CriteriaVo("e", 0, 10);
        IbaModel.getInstance().getProductSearchList(criteriaVo, mProductList,mResponseCode);
    }


    public LiveData<List<ProductVo>> getProductList() {
        return mProductList;
    }

    public void showTokenError(Integer errorCode){
        mView.showTokenError(errorCode);
    }


    @Override
    public void onTapView(Double infoId) {
        mView.showProductDetail(infoId);
    }

    @Override
    public void onTapSearch() {
        Log.e("goProductSearchScreen", "goProductSearchScreen: ");
        mView.goProductSearchScreen();
    }

    @Override
    public void onTapShoppingCart() {
        mView.onTapShoppingCart();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccessByRefreshToken(TokenEvent event){
        int responseCode = event.getResponseCode();
        if(responseCode==200){
            forceRefresh();
        }else {
            showTokenError(responseCode);
        }
    }
}
