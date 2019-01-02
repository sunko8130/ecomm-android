package com.creative_webstudio.iba.networks.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.criterias.ProductCriteria;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by zeyar on 12/19/18.
 */

public class ProductSearchViewModel extends AndroidViewModel {
    public ProductSearchViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<ApiResponse<List<ProductVO>>> getProductSearchList(ProductCriteria criteriaVO) {
        MutableLiveData<ApiResponse<List<ProductVO>>> result = new MutableLiveData<>();
        ApiResponse<List<ProductVO>> apiResponse = new ApiResponse();

        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getProductSearch(userAuth, criteriaVO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<ProductVO> body = response.body();
                    if (response.isSuccessful() && body != null && !body.isEmpty()) {
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


}
