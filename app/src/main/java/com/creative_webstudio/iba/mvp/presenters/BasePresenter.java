package com.creative_webstudio.iba.mvp.presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.creative_webstudio.iba.datas.vos.ProductVo;
import com.creative_webstudio.iba.mvp.views.BaseView;

public abstract class BasePresenter<T extends BaseView> extends ViewModel {

    protected T mView;
    protected MutableLiveData<String> mErrorLD;

    protected MutableLiveData<Integer> mResponseCode;
    protected MutableLiveData<ProductVo> mProuductSearchVo;

    public void initPresenter(final T mView) {
        this.mView = mView;
        mErrorLD = new MutableLiveData<>();
        mResponseCode = new MutableLiveData<>();
        mProuductSearchVo = new MutableLiveData<>();
    }

    public LiveData<String> getErrorLD() {
        return mErrorLD;
    }

    public LiveData<Integer> getResponseCode() {
        return mResponseCode;
    }
}
