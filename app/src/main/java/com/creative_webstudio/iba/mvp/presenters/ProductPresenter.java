package com.creative_webstudio.iba.mvp.presenters;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.datas.vos.CriteriaVo;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.delegates.ProductDelegate;
import com.creative_webstudio.iba.enents.TokenEvent;
import com.creative_webstudio.iba.mvp.views.ProductView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

public class ProductPresenter extends BasePresenter<ProductView> implements ProductDelegate {

    private MutableLiveData<List<ProductVO>> mProductList;
    private int mOffset = 0;
    @Override
    public void initPresenter(ProductView mView) {
        super.initPresenter(mView);
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        mProductList = new MutableLiveData<>();
    }

    public void getProduct(int page) {
        int limit=2;
        mOffset =limit*page;
        CriteriaVo criteriaVo = new CriteriaVo(mOffset, limit);
        IbaModel.getInstance().getProductByPaging(criteriaVo, mProductList,mResponseCode);
//        IbaModel.getInstance().getProduct(criteriaVo, mApiResposne);
    }



    public LiveData<List<ProductVO>> getProductList() {
        return mProductList;
    }


    public void showTokenError(Integer errorCode){
        mView.showTokenError(errorCode);
    }


    @Override
    public void onTapView(Long infoId) {
        mView.showProductDetail(infoId);
    }

    @Override
    public void onTapSearch() {
        Log.e("goProductSearchScreen", "goProductSearchScreen: ");
        mView.goProductSearchScreen();
    }



    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onAccessByRefreshToken(TokenEvent event) {
        int responseCode = event.getResponseCode();
        if(responseCode==200){
            getProduct(mOffset);
        }else {
            showTokenError(responseCode);
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus eventBus = EventBus.getDefault();
        eventBus.unregister(this);
    }
}
