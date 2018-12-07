package com.creative_webstudio.iba.datas.models;

import android.accounts.NetworkErrorException;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.creative_webstudio.iba.datas.vos.CriteriaVo;
import com.creative_webstudio.iba.datas.vos.HCInfoVO;
import com.creative_webstudio.iba.datas.vos.ProductVo;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.enents.TokenEvent;
import com.creative_webstudio.iba.networks.HCInfoResponse;
import com.creative_webstudio.iba.utils.AppConstants;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class IbaModel extends BaseModel {
    public static IbaModel objInstance;
    public List<HCInfoVO> infoVOList;
    IBAPreferenceManager ibaPreference;

    protected IbaModel(Context context) {
        super(context);
        infoVOList = new ArrayList<>();
        ibaPreference = new IBAPreferenceManager(context);
        EventBus.getDefault().register(this);
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

    public void getTokenByUP(String userName, String password, final MutableLiveData<Integer> mResponseCode) {
        String base = AppConstants.CLIENT_ID + ":" + AppConstants.CLIENT_SECRET;
        String userAuth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        theApi.getTokenByUP(userAuth, userName, password, AppConstants.GRANT_TYPE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<TokenVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<TokenVO> response) {
                        if (response.code() == 200) {
                            Log.e("auth", "onNext: " + response.code());
                            TokenVO tokenVO = (TokenVO) response.body();
                            ibaPreference.toPreference("AccessToken", tokenVO.getAccessToken());
                            ibaPreference.toPreference("RefreshToken", tokenVO.getRefreshToken());
                            mResponseCode.setValue(response.code());
                        } else if (response.code() == 204) {
                            //no data
                            mResponseCode.setValue(response.code());
                            Log.e("auth", "noData: " + response.code());
                        } else {
                            mResponseCode.setValue(300);
                            Log.e("auth", "severError: " + response.code());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //network error
                        if (e instanceof IOException) {
                            mResponseCode.setValue(666);
                        } else if (e instanceof NetworkErrorException) {
                            mResponseCode.setValue(777);
                        } else {
                            mResponseCode.setValue(888);
                        }
                        Log.e("auth", "onError: " + e.getMessage());
                        Log.e("auth", "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void getTokenByRefresh(final CriteriaVo criteriaVo, final MutableLiveData<List<ProductVo>> productSearList, final MutableLiveData<Integer>responseCode) {
        String refreshToken = ibaPreference.fromPreference("RefreshToken","");
        String base = AppConstants.CLIENT_ID + ":" + AppConstants.CLIENT_SECRET;
        String userAuth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
        theApi.getTokenByRefresh(userAuth, refreshToken, "refresh_token")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<TokenVO>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<TokenVO> response) {
                        if (response.code() == 200) {
                            Log.e("auth", "onNext: " + response.code());
                            TokenVO tokenVO = response.body();
                            ibaPreference.toPreference("AccessToken", tokenVO.getAccessToken());
                            ibaPreference.toPreference("RefreshToken", tokenVO.getRefreshToken());
                            TokenEvent event = new TokenEvent(response.code());
                            EventBus.getDefault().post(event);

                        } else if (response.code() == 204) {
                            //no data
                            TokenEvent event = new TokenEvent(response.code());
                            EventBus.getDefault().post(event);
                        } else {
                            TokenEvent event = new TokenEvent(300);
                            EventBus.getDefault().post(event);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //network error
                        if (e instanceof IOException) {
                            TokenEvent event = new TokenEvent(666);
                            EventBus.getDefault().post(event);
                        } else if (e instanceof NetworkErrorException) {
                            TokenEvent event = new TokenEvent(777);
                            EventBus.getDefault().post(event);
                        } else {
                            TokenEvent event = new TokenEvent(888);
                            EventBus.getDefault().post(event);
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void getProductSearchList(final CriteriaVo criteriaVo, final MutableLiveData<List<ProductVo>> productSearList, final MutableLiveData<Integer>responseCode) {
        String base = ibaPreference.fromPreference("AccessToken", "");
        String userAuth = "Bearer " + base;
        theApiProductSearch.getProductSearch(userAuth, criteriaVo)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Response<List<ProductVo>>>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Response<List<ProductVo>> listResponse) {
                        if(listResponse.code()==401){
                            getTokenByRefresh(criteriaVo,productSearList,responseCode);
                        }else if (listResponse.code() == 200) {
                           productSearList.setValue(listResponse.body());
                        } else if (listResponse.code() == 204) {
                            //no data
                            responseCode.setValue(listResponse.code());
                            Log.e("auth", "noData: " + listResponse.code());
                        } else {
                            responseCode.setValue(300);
                            Log.e("auth", "severError: " + listResponse.code());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //network error
                        if (e instanceof IOException) {
                            responseCode.setValue(666);
                        } else if (e instanceof NetworkErrorException) {
                            responseCode.setValue(777);
                        } else {
                            responseCode.setValue(888);
                        }

                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }

    public void loadHCInfo(final MutableLiveData<List<HCInfoVO>> mHCInfoList,
                           final MutableLiveData<String> error) {
        theApiSample.loadHCInfo("b002c7e1a528b7cb460933fc2875e916")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HCInfoResponse>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(HCInfoResponse hcInfoResponse) {
                        if (hcInfoResponse != null) {
                            infoVOList = hcInfoResponse.getInfoVOList();
                            mHCInfoList.setValue(hcInfoResponse.getInfoVOList());
                        } else {
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

    public HCInfoVO getInfoById(double infoId) {
        HCInfoVO info = null;
        for (HCInfoVO i : infoVOList) {
            if (i.getId() == infoId) info = i;
        }
        return info;
    }


}
