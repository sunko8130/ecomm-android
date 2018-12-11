package com.creative_webstudio.iba.networks;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.vos.ProductPagingVO;
import com.creative_webstudio.iba.exception.ApiException;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ProductViewModel extends ViewModel {

    public MutableLiveData<ApiResponse<ProductPagingVO>> getProduct(int page) {
        MutableLiveData<ApiResponse<ProductPagingVO>> result = new MutableLiveData<>();
        ApiResponse<ProductPagingVO> apiResponse = new ApiResponse();
        IbaAPI service = ServiceGenerator.createService(IbaAPI.class);
        service.getProduct(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    ProductPagingVO body = response.body();
                    if (response.isSuccessful()) {
                        if (body != null && body.getProductVOList() != null && !body.getProductVOList().isEmpty()) {
                            apiResponse.setData(body);
                        } else {
                            apiResponse.setError(new ApiException(1));
                        }
                    } else {
                        apiResponse.setError(new ApiException(response.code()));
                    }
                }, throwable -> {
                    apiResponse.setError(new ApiException(throwable));
                });

        result.setValue(apiResponse);
        return result;
    }
}
