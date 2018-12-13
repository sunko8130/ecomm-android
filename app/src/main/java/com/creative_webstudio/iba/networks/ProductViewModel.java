package com.creative_webstudio.iba.networks;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.vos.ProductPagingVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProductViewModel extends AndroidViewModel {

    public ProductViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<ApiResponse<ProductPagingVO>> getProduct(int page) {
        MutableLiveData<ApiResponse<ProductPagingVO>> result = new MutableLiveData<>();
        ApiResponse<ProductPagingVO> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        api.getProduct(accessToken, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    ProductPagingVO body = response.body();
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
