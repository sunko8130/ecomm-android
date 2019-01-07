package com.creative_webstudio.iba.networks.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.criterias.CategoryCriteria;
import com.creative_webstudio.iba.datas.criterias.ThumbnailCriteria;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.datas.criterias.ProductCriteria;
import com.creative_webstudio.iba.datas.vos.ProductResponse;
import com.creative_webstudio.iba.exception.ApiException;
import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.networks.ServiceGenerator;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
        ProductCriteria criteriaVO = new ProductCriteria();
        ThumbnailCriteria thumbnailCriteria = new ThumbnailCriteria();
        thumbnailCriteria.setThumbnailType(1);
        if(categoryId == -1) {
            criteriaVO.setPageNumber(String.valueOf(page));
            criteriaVO.setWithOrderUnit(true);
            criteriaVO.setProductCategoryId(null);
            criteriaVO.setThumbnail(thumbnailCriteria);
            criteriaVO.setWithThumbnail(true);
            criteriaVO.setWithDetail(true);
        }else {
            criteriaVO.setPageNumber(String.valueOf(page));
            criteriaVO.setWithOrderUnit(true);
            criteriaVO.setThumbnail(thumbnailCriteria);
            criteriaVO.setWithThumbnail(true);
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
        CategoryCriteria criteriaVO = new CategoryCriteria();
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


}
