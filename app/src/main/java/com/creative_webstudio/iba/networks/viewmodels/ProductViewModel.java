package com.creative_webstudio.iba.networks.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.vos.CriteriaVO;
import com.creative_webstudio.iba.datas.vos.ProductResponse;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProductViewModel extends AndroidViewModel {
    private int mOffset = 0;

    public ProductViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<ApiResponse<ProductResponse>> getProduct(int page) {
        CriteriaVO criteriaVO = new CriteriaVO();
        criteriaVO.setPageNumber(page);
        criteriaVO.setWithOrderUnits(true);
        MutableLiveData<ApiResponse<ProductResponse>> result = new MutableLiveData<>();
        ApiResponse<ProductResponse> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getProduct(userAuth, criteriaVO)
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
}
