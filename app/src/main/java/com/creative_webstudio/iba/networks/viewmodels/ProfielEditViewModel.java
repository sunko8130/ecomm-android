package com.creative_webstudio.iba.networks.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.criterias.CustomerCriteria;
import com.creative_webstudio.iba.datas.vos.RegionVO;
import com.creative_webstudio.iba.datas.vos.TownshipVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProfielEditViewModel extends AndroidViewModel {
    IBAPreferenceManager prefs;
    public ProfielEditViewModel(@NonNull Application application) {
        super(application);
        prefs = new IBAPreferenceManager(getApplication());
    }

    public MutableLiveData<ApiResponse<List<RegionVO>>> getRegion() {
        MutableLiveData<ApiResponse<List<RegionVO>>> result = new MutableLiveData<>();
        ApiResponse<List<RegionVO>> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getRegion(userAuth)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<RegionVO> body = response.body();
                    if (response.isSuccessful()
                            && body != null) {
                        apiResponse.setData(body);
                    } else {
                        apiResponse.setError(new ApiException(response.code()));
                    }
                    result.setValue(apiResponse);
                }, throwable -> {
                    apiResponse.setError(new ApiException(throwable));
                    result.setValue(apiResponse);
                });
        return result;
    }

    public MutableLiveData<ApiResponse<List<TownshipVO>>> getTownship(String regionCode) {
        MutableLiveData<ApiResponse<List<TownshipVO>>> result = new MutableLiveData<>();
        ApiResponse<List<TownshipVO>> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getTownShip(userAuth,regionCode)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<TownshipVO> body = response.body();
                    if (response.isSuccessful()
                            && body != null) {
                        apiResponse.setData(body);
                    } else {
                        apiResponse.setError(new ApiException(response.code()));
                    }
                    result.setValue(apiResponse);
                }, throwable -> {
                    apiResponse.setError(new ApiException(throwable));
                    result.setValue(apiResponse);
                });
        return result;
    }

    public MutableLiveData<ApiResponse<Integer>> updateCustomer(CustomerCriteria criteria) {
        MutableLiveData<ApiResponse<Integer>> result = new MutableLiveData<>();
        ApiResponse<Integer> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.updateCustomerInfo(userAuth,criteria)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        apiResponse.setData(response.body());
                    } else {
                        apiResponse.setError(new ApiException(response.code()));
                    }
                    result.setValue(apiResponse);
                }, throwable -> {
                    apiResponse.setError(new ApiException(throwable));
                    result.setValue(apiResponse);
                });
        return result;
    }

    public MutableLiveData<ApiResponse<Integer>> updatePassword(String oldPassword,String newPassword) {
        MutableLiveData<ApiResponse<Integer>> result = new MutableLiveData<>();
        ApiResponse<Integer> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.updatePassword(userAuth,oldPassword,newPassword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    if (response.isSuccessful()) {
                        apiResponse.setData(response.body());
                    } else {
                        apiResponse.setError(new ApiException(response.code()));
                    }
                    result.setValue(apiResponse);
                }, throwable -> {
                    apiResponse.setError(new ApiException(throwable));
                    result.setValue(apiResponse);
                });
        return result;
    }
}
