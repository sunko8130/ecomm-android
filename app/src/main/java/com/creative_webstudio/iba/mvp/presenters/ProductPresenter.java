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

    @Override
    public void initPresenter(ProductView mView) {
        super.initPresenter(mView);
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
        mProductList = new MutableLiveData<>();
        forceRefresh();
    }

    public void forceRefresh() {
        CriteriaVo criteriaVo = new CriteriaVo(0, 10);
<<<<<<< Updated upstream
        IbaModel.getInstance().getProductPaging(criteriaVo, mProductList,mResponseCode);
=======
        criteriaVo.setWord("e");
        IbaModel.getInstance().getProductSearchList(criteriaVo, mProductList, mResponseCode);
>>>>>>> Stashed changes
    }


    public LiveData<List<ProductVO>> getProductList() {
        return mProductList;
    }

    public void showTokenError(Integer errorCode) {
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
    public void onAccessByRefreshToken(TokenEvent event) {
        int responseCode = event.getResponseCode();
        if (responseCode == 200) {
            forceRefresh();
        } else {
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
