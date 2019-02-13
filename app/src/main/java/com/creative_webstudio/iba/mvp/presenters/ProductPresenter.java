package com.creative_webstudio.iba.mvp.presenters;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.mvp.views.ProductView;

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
}
