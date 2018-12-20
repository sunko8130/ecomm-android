package com.creative_webstudio.iba.networks.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.vos.OrderHistoryCriteriaVO;
import com.creative_webstudio.iba.datas.vos.OrderHistoryResponse;
import com.creative_webstudio.iba.datas.vos.OrderItemCriteria;
import com.creative_webstudio.iba.datas.vos.OrderItemVO;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class OrderHistoryViewModel extends AndroidViewModel {
    IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
    public OrderHistoryViewModel(@NonNull Application application) {
        super(application);
    }
    public MutableLiveData<ApiResponse<OrderHistoryResponse>> getOrderHistory(int page) {
        // TODO: Handle page including criteria
        OrderHistoryCriteriaVO criteriaVO = new OrderHistoryCriteriaVO();
        criteriaVO.setWithOrderItems(true);
        MutableLiveData<ApiResponse<OrderHistoryResponse>> result = new MutableLiveData<>();
        ApiResponse<OrderHistoryResponse> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getOrderHistory(userAuth, criteriaVO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    OrderHistoryResponse body = response.body();
                    if (response.isSuccessful()
                            && body != null
                            && body.getOrderHistoryList() != null
                            && !body.getOrderHistoryList().isEmpty()) {
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

    public MutableLiveData<ApiResponse<List<OrderItemVO>>> getOrderItems(long[] itemIds){
        MutableLiveData<ApiResponse<List<OrderItemVO>>> result = new MutableLiveData<>();
        ApiResponse<List<OrderItemVO>> apiResponse = new ApiResponse();
        OrderItemCriteria criteria = new OrderItemCriteria();
        criteria.setIncludeIds(itemIds);
        criteria.setThumbnailType(1);
        criteria.setWithOrderUnit(true);
        criteria.setWithProduct(true);
        criteria.setWithThumbnails(true);

        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getOrderItems(userAuth,criteria)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<OrderItemVO> body = response.body();
                    if (response.isSuccessful()
                            && body != null
                            && !body.isEmpty()) {
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

    public MutableLiveData<ApiResponse<Integer>> cancelOrder(long orderId) {
        MutableLiveData<ApiResponse<Integer>> result = new MutableLiveData<>();
        ApiResponse<Integer> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.updateOrder(userAuth,orderId,"CANCELED")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    int body = response.body();
                    if (response.isSuccessful()) {
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
