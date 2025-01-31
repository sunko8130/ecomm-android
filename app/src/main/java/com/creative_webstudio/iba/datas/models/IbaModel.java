package com.creative_webstudio.iba.datas.models;

import android.accounts.NetworkErrorException;
import android.arch.lifecycle.MutableLiveData;
import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.criterias.ConfigurationCriteria;
import com.creative_webstudio.iba.datas.criterias.ProductCriteria;
import com.creative_webstudio.iba.datas.vos.ConfigurationVO;
import com.creative_webstudio.iba.datas.vos.CustomerVO;
import com.creative_webstudio.iba.datas.vos.LogOutVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.enents.TokenEvent;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.AppConstants;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class IbaModel extends BaseModel {

    public static IbaModel objInstance;
    IBAPreferenceManager ibaPreference;
    private CustomerVO customerVO;

    protected IbaModel(Context context) {
        super(context);
        ibaPreference = new IBAPreferenceManager(context);

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

    public MutableLiveData<ApiResponse<CustomerVO>> getCustomer(){
        MutableLiveData<ApiResponse<CustomerVO>> result = new MutableLiveData<>();
        ApiResponse<CustomerVO> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        String accessToken = ibaPreference.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getCustomerInfo(userAuth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    CustomerVO body = response.body();
                    if (response.isSuccessful()
                            && body != null) {
                        apiResponse.setData(body);
                        customerVO = body;
                    } else {
                        apiResponse.setError(new ApiException(response.code()));
                    }
                    result.setValue(apiResponse);
                }, throwable -> {
                    Crashlytics.logException(throwable);
                    apiResponse.setError(new ApiException(throwable));
                    result.setValue(apiResponse);
                });

        return result;
    }

    public MutableLiveData<ApiResponse<LogOutVO>> userLogOut(){
        MutableLiveData<ApiResponse<LogOutVO>> result = new MutableLiveData<>();
        ApiResponse<LogOutVO> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        String accessToken = ibaPreference.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.userLogOut(userAuth,accessToken)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    LogOutVO body = response.body();
                    if (response.isSuccessful()
                            && body != null) {
                        apiResponse.setData(body);
                    } else {
                        apiResponse.setError(new ApiException(response.code()));
                    }
                    result.setValue(apiResponse);
                }, throwable -> {
                    Crashlytics.logException(throwable);
                    apiResponse.setError(new ApiException(throwable));
                    result.setValue(apiResponse);
                });

        return result;
    }

    public MutableLiveData<ApiResponse<List<ConfigurationVO>>> getConfigData(String settingKey){
        MutableLiveData<ApiResponse<List<ConfigurationVO>>> result = new MutableLiveData<>();
        ApiResponse<List<ConfigurationVO>> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        api.getConfigurationInfo(settingKey)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<ConfigurationVO> body = response.body();
                    if (response.isSuccessful()
                            && body != null && !body.isEmpty()) {
                        apiResponse.setData(body);
                    } else {
                        apiResponse.setError(new ApiException(response.code()));
                    }
                    result.setValue(apiResponse);
                }, throwable -> {
                    Crashlytics.logException(throwable);
                    apiResponse.setError(new ApiException(throwable));
                    result.setValue(apiResponse);
                });

        return result;
    }


    public CustomerVO getCustomerVO(){
        return customerVO;
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
                        if (response.isSuccessful()) {
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
                            }
                        } else {
                            mResponseCode.setValue(400);
                            Log.e("auth", "severError: " + response.code());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        //network error
                        if (e instanceof IOException) {
//                            TokenEvent event = new TokenEvent(AppConstants.ERROR_IOEXCEPTION);
//                            EventBus.getDefault().post(event);
                            mResponseCode.setValue(AppConstants.ERROR_IOEXCEPTION);
                        } else if (e instanceof NetworkErrorException) {
//                            TokenEvent event = new TokenEvent(AppConstants.ERROR_NETWORK);
//                            EventBus.getDefault().post(event);
                            mResponseCode.setValue(AppConstants.ERROR_IOEXCEPTION);
                        } else {
//                            TokenEvent event = new TokenEvent(AppConstants.ERROR_UNKNOWN);
//                            EventBus.getDefault().post(event);
                            mResponseCode.setValue(AppConstants.ERROR_IOEXCEPTION);
                        }
                        Log.e("auth", "onError: " + e.getMessage());
                        Log.e("auth", "onError: " + e.getLocalizedMessage());
                    }

                    @Override
                    public void onComplete() {

                    }
                });

    }



//    public void getTokenByRefresh() {
//        String refreshToken = ibaPreference.fromPreference("RefreshToken", "");
//        String base = AppConstants.CLIENT_ID + ":" + AppConstants.CLIENT_SECRET;
//        String userAuth = "Basic " + Base64.encodeToString(base.getBytes(), Base64.NO_WRAP);
//        theApi.getTokenByRefresh(userAuth, refreshToken, "refresh_token")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Response<TokenVO>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Response<TokenVO> response) {
//                        if (response.code() == 200) {
//                            Log.e("auth", "onNext: " + response.code());
//                            TokenVO tokenVO = response.body();
//                            ibaPreference.toPreference("AccessToken", tokenVO.getAccessToken());
//                            ibaPreference.toPreference("RefreshToken", tokenVO.getRefreshToken());
//                            TokenEvent event = new TokenEvent(response.code());
//                            EventBus.getDefault().post(event);
//
//                        } else if (response.code() == AppConstants.ERROR_NODATA) {
//                            //no data
//                            TokenEvent event = new TokenEvent(response.code());
//                            EventBus.getDefault().post(event);
//                        } else {
//                            TokenEvent event = new TokenEvent(AppConstants.ERROR_ACCESSTOKEN);
//                            EventBus.getDefault().post(event);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //network error
//                        if (e instanceof IOException) {
//                            TokenEvent event = new TokenEvent(AppConstants.ERROR_IOEXCEPTION);
//                            EventBus.getDefault().post(event);
//                        } else if (e instanceof NetworkErrorException) {
//                            TokenEvent event = new TokenEvent(AppConstants.ERROR_NETWORK);
//                            EventBus.getDefault().post(event);
//                        } else {
//                            TokenEvent event = new TokenEvent(AppConstants.ERROR_UNKNOWN);
//                            EventBus.getDefault().post(event);
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }

//    public void getProductSearchList(final ProductCriteria criteriaVO, final MutableLiveData<List<ProductVO>> productSearList, final MutableLiveData<Integer> responseCode) {
//        String base = ibaPreference.fromPreference("AccessToken", "");
//        String userAuth = "Bearer " + base;
//        theApiProductSearch.getProductSearch(userAuth, criteriaVO)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Response<List<ProductVO>>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Response<List<ProductVO>> listResponse) {
//                        if (listResponse.code() == 401) {
//                            getTokenByRefresh();
//                        } else if (listResponse.code() == 200) {
//                            Log.e("auth", "getProductSearch onNext: " + listResponse.body().size());
//                            productSearList.setValue(listResponse.body());
//                        } else if (listResponse.code() == 204) {
//                            //no data
//                            responseCode.setValue(listResponse.code());
//                            Log.e("auth", "noData: " + listResponse.code());
//                        } else {
//                            responseCode.setValue(300);
//                            Log.e("auth", "severError: " + listResponse.code());
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        //network error
//                        if (e instanceof IOException) {
//                            responseCode.setValue(666);
//                        } else if (e instanceof NetworkErrorException) {
//                            responseCode.setValue(777);
//                        } else {
//                            responseCode.setValue(888);
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//
//
//                });
//
//    }



//    public void getProduct(ConfigurationCriteria criteriaVO, final MutableLiveData<List<ProductVO>> mProductList, final MutableLiveData<Integer> responseCode) {
//        String base = ibaPreference.fromPreference("AccessToken", "");
//        String auth = "Bearer " + base;
//        theApiProductSearch.getProductPaging(auth, criteriaVO)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Response<ProductResponse>>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(Response<ProductResponse> response) {
//                        if (response.code() == 401) {
//                            // Access token expire.
//                            getTokenByRefresh();
//                        } else if (response.code() == 200) {
//                            mProductList.setValue(response.body().getProductVOList());
//                        } else if (response.code() == 204) {
//                            // Response has no data
//                            responseCode.setValue(response.code());
//                        } else {
//                            responseCode.setValue(300);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        if (e instanceof IOException) {
//                            responseCode.setValue(666);
//                        } else if (e instanceof NetworkErrorException) {
//                            responseCode.setValue(777);
//                        } else {
//                            responseCode.setValue(888);
//                        }
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//    }



}
