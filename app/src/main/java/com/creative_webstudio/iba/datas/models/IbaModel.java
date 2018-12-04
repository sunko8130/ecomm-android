package com.creative_webstudio.iba.datas.models;

import android.arch.lifecycle.MutableLiveData;
import android.content.Context;

import com.creative_webstudio.iba.datas.vos.HCInfoVO;
import com.creative_webstudio.iba.networks.HCInfoResponse;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class IbaModel extends BaseModel {
    public static IbaModel objInstance;
    public List<HCInfoVO> infoVOList;

    protected IbaModel(Context context) {
        super(context);
        infoVOList = new ArrayList<>();
    }
    public static void initAppModel(Context context) {
        objInstance = new IbaModel(context);
    }

    public static IbaModel getInstance() {
        if (objInstance == null) {
            throw new RuntimeException("IbaModel is being invoked before initializing.");
        }
        return objInstance;
    }

    public void loadHCInfo(final MutableLiveData<List<HCInfoVO>> mHCInfoList , final MutableLiveData<String> error){
        theApi.loadHCInfo("b002c7e1a528b7cb460933fc2875e916")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HCInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HCInfoResponse hcInfoResponse) {
                        if(hcInfoResponse!=null){
                            infoVOList = hcInfoResponse.getInfoVOList();
                            mHCInfoList.setValue(hcInfoResponse.getInfoVOList());
                        }else {
                            error.setValue("NoData");
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        error.setValue(e.getMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public HCInfoVO getInfoById(double infoId){
        HCInfoVO info=null;
        for(HCInfoVO i: infoVOList ){
            if(i.getId()==infoId)info=i;
        }
        return info;
    }

}
