package com.creative_webstudio.iba.networks.viewmodels;

import android.accounts.NetworkErrorException;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.vos.CategoryCriteriaVO;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.datas.vos.OrderHistoryCriteria;
import com.creative_webstudio.iba.datas.vos.OrderHistoryVO;
import com.creative_webstudio.iba.datas.vos.ProductCriteriaVO;
import com.creative_webstudio.iba.datas.vos.ProductResponse;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ProductViewModel extends AndroidViewModel {

    public ProductViewModel(Application application) {
        super(application);
    }

    public MutableLiveData<ApiResponse<ProductResponse>> getProduct(int page) {
        return getProduct(page, -1);
    }

    /**
     * @param categoryId The category id or -1 to ignore to get all products.
     */
    public MutableLiveData<ApiResponse<ProductResponse>> getProduct(int page, long categoryId) {
        ProductCriteriaVO criteriaVO = new ProductCriteriaVO();
        if(categoryId == -1) {
            criteriaVO.setPageNumber(page);
            criteriaVO.setWithOrderUnits(true);
            criteriaVO.setProductCategoryId(null);
            criteriaVO.setThumbnailType(1);
            criteriaVO.setWithThumbnails(true);
        }else {
            criteriaVO.setPageNumber(page);
            criteriaVO.setWithOrderUnits(true);
            criteriaVO.setThumbnailType(1);
            criteriaVO.setWithThumbnails(true);
            criteriaVO.setProductCategoryId(String.valueOf(categoryId));
        }
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

    public MutableLiveData<ApiResponse<List<CategoryVO>>> getCategory() {
        CategoryCriteriaVO criteriaVO = new CategoryCriteriaVO();
        criteriaVO.setType("MAIN");
        MutableLiveData<ApiResponse<List<CategoryVO>>> result = new MutableLiveData<>();
        ApiResponse<List<CategoryVO>> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getCategory(userAuth, criteriaVO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<CategoryVO> body = response.body();
                    if (response.isSuccessful() && body != null && body != null && !body.isEmpty()) {
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

    public MutableLiveData<ApiResponse<OrderHistoryVO>> getOrderHistory(int page) {
        // TODO: Handle page including criteria
        OrderHistoryCriteria criteriaVO = new OrderHistoryCriteria();
        MutableLiveData<ApiResponse<OrderHistoryVO>> result = new MutableLiveData<>();
        ApiResponse<OrderHistoryVO> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getOrderHistory(userAuth, criteriaVO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    OrderHistoryVO body = response.body();
                    if (response.isSuccessful()
                            && body != null && body != null
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
}
