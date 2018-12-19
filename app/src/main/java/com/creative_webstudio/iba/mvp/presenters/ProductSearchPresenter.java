package com.creative_webstudio.iba.mvp.presenters;

import android.arch.lifecycle.MutableLiveData;

import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.delegates.ProductSearchDelegate;
import com.creative_webstudio.iba.mvp.views.ProductSearchView;

import java.util.List;

public class ProductSearchPresenter extends BasePresenter<ProductSearchView> implements ProductSearchDelegate {

    private MutableLiveData<List<ProductVO>> mListMutableLiveData;

    @Override
    public void initPresenter(ProductSearchView mView) {
        super.initPresenter(mView);
        mListMutableLiveData = new MutableLiveData<>();

    }

    @Override
    public void onTapView() {
        mView.onTapView();
    }

    @Override
    public void onTapSearch(String word) {
//        CriteriaVo criteriaVo = new CriteriaVo(0, 10);
//        criteriaVo.setWord(word);
//       IbaModel.getInstance().getProductSearchList(criteriaVo, mListMutableLiveData, mResponseCode);
    }

    public MutableLiveData<List<ProductVO>> getmListMutableLiveData() {
        return mListMutableLiveData;
    }

    public void setmListMutableLiveData(MutableLiveData<List<ProductVO>> mListMutableLiveData) {
        this.mListMutableLiveData = mListMutableLiveData;
    }
}
