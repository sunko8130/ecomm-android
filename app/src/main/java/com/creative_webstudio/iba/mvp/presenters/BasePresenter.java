package com.creative_webstudio.iba.mvp.presenters;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.mvp.views.BaseView;

import org.greenrobot.eventbus.EventBus;

public abstract class BasePresenter<T extends BaseView> extends ViewModel {

    protected T mView;
    protected MutableLiveData<String> mErrorLD;

    protected MutableLiveData<Integer> mResponseCode;
    protected MutableLiveData<ProductVO> mProuductSearchVo;

    public void initPresenter(final T mView) {
        this.mView = mView;
        mErrorLD = new MutableLiveData<>();
        mResponseCode = new MutableLiveData<>();
        mProuductSearchVo = new MutableLiveData<>();
        EventBus eventBus = EventBus.getDefault();
        if (!eventBus.isRegistered(this)) {
            eventBus.register(this);
        }
    }

    public LiveData<String> getErrorLD() {
        return mErrorLD;
    }

    public LiveData<Integer> getResponseCode() {
        return mResponseCode;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        EventBus eventBus = EventBus.getDefault();
        eventBus.unregister(this);
    }
}
