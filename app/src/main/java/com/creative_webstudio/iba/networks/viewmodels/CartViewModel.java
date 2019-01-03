package com.creative_webstudio.iba.networks.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.criterias.ProductCriteria;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.vos.ProductResponse;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class CartViewModel extends AndroidViewModel {

    public CartViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ApiResponse<ProductResponse>> getProductByID(int page, long[] productId) {
        MutableLiveData<ApiResponse<ProductResponse>> result = new MutableLiveData<>();
        ProductCriteria criteriaVO = new ProductCriteria();
        criteriaVO.setIncludeIds(productId);
        criteriaVO.setPageNumber(String.valueOf(page));
        criteriaVO.setWithThumbnails(true);
        criteriaVO.setThumbnailType(1);
        criteriaVO.setWithOrderUnits(true);
        ApiResponse<ProductResponse> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getProductById(userAuth, criteriaVO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    ProductResponse body = response.body();
                    if (response.isSuccessful() && body != null && body.getProductVOList() != null && !body.getProductVOList().isEmpty()) {
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

    public MutableLiveData<ApiResponse<Object>> sendOrder(List<CartVO> criteria) {
        MutableLiveData<ApiResponse<Object>> result = new MutableLiveData<>();
        ApiResponse<Object> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.sendOrder(userAuth, criteria)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            apiResponse.setData(response.body());
                        } else if (response.code() == 405) {
                            Gson gson = new Gson();
                            String resp = response.errorBody().string();
                            List<CartVO> orderResponseVO =  gson.fromJson(resp, new TypeToken<List<CartVO>>(){}.getType());
                            apiResponse.setData(orderResponseVO);
                        } else {
                            apiResponse.setError(new ApiException(response.code()));
                        }
                        result.setValue(apiResponse);
                    } catch (Exception e) {
                        apiResponse.setError(new ApiException(e));
                        result.setValue(apiResponse);
                    }

                }, throwable -> {
                    apiResponse.setError(new ApiException(throwable));
                    result.setValue(apiResponse);
                });

        return result;
    }
}
