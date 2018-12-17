package com.creative_webstudio.iba.networks.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.support.annotation.NonNull;

public class ProductDetailViewModel extends AndroidViewModel {

    public ProductDetailViewModel(@NonNull Application application) {
        super(application);
    }

//    public MutableLiveData<ApiResponse<OrderUnitResponse>> getOrderUnit(long productId) {
//        CriteriaVO criteriaVO = new CriteriaVO();
//        criteriaVO.setProductId(productId);
//        MutableLiveData<ApiResponse<OrderUnitResponse>> result = new MutableLiveData<>();
//        ApiResponse<OrderUnitResponse> apiResponse = new ApiResponse();
//        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
//        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
//        String accessToken = prefs.getAccessToken();
//        String userAuth = "Bearer " + accessToken;
//        api.getOrderUnit(userAuth, criteriaVO)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(response ->{
//                    List<OrderUnitVO> body = response.body();
//                    OrderUnitResponse orderUnitResponse=new OrderUnitResponse();
//                    orderUnitResponse.setOrderUnitVOS(body);
//                    if (response.isSuccessful() && body != null && body!= null && !body.isEmpty()) {
//                        apiResponse.setData(orderUnitResponse);
//                    } else {
//                        apiResponse.setError(new ApiException(response.code()));
//                    }
//                    result.setValue(apiResponse);
//                }, throwable -> {
//                    apiResponse.setError(new ApiException(throwable));
//                    result.setValue(apiResponse);
//                });
//
//        return result;
//    }
}
