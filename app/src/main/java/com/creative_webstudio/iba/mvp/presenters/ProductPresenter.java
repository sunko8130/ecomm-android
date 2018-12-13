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

public class ProductPresenter extends BasePresenter<ProductView> {

    private MutableLiveData<List<ProductVO>> mProductList;
    private int mOffset = 0;

    @Override
    public void initPresenter(ProductView mView) {
        super.initPresenter(mView);
        mProductList = new MutableLiveData<>();
    }

    public void getProduct(int page) {
        int limit = 2;
        mOffset = limit * page;
//        CriteriaVo criteriaVo = new CriteriaVo(mOffset, limit);
//        IbaModel.getInstance().getProductByPaging(criteriaVo, mProductList, mResponseCode);
    }

    public LiveData<List<ProductVO>> getProductList() {
        return mProductList;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus eventBus = EventBus.getDefault();
        eventBus.unregister(this);
    }
}
