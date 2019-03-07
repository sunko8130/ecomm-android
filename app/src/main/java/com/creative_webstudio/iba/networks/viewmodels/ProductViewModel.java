package com.creative_webstudio.iba.networks.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;

import com.creative_webstudio.iba.datas.ApiResponse;
import com.creative_webstudio.iba.datas.criterias.AdvertisementCriteria;
import com.creative_webstudio.iba.datas.criterias.BrandCriteria;
import com.creative_webstudio.iba.datas.criterias.CategoryCriteria;
import com.creative_webstudio.iba.datas.criterias.OrderUnitCriteria;
import com.creative_webstudio.iba.datas.criterias.PromoRewardDetailCriteria;
import com.creative_webstudio.iba.datas.criterias.ThumbnailCriteria;
import com.creative_webstudio.iba.datas.vos.AdvertisementVO;
import com.creative_webstudio.iba.datas.vos.BrandVO;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.datas.criterias.ProductCriteria;
import com.creative_webstudio.iba.datas.vos.ProductResponse;
import com.creative_webstudio.iba.datas.vos.PromoRewardDetailVO;
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
        thumbnailCriteria.setThumbnailType(2);
        OrderUnitCriteria orderUnitCriteria = new OrderUnitCriteria();
        orderUnitCriteria.setWithPromoReward(true);
        criteriaVO.setOrderUnit(orderUnitCriteria);
        if (categoryId == -2) {
            criteriaVO.setPageNumber(String.valueOf(page));
            criteriaVO.setWithOrderUnit(true);
            criteriaVO.setProductCategoryId(null);
            criteriaVO.setThumbnail(thumbnailCriteria);
            criteriaVO.setWithThumbnail(true);
            criteriaVO.setWithDetail(true);
        } else if (categoryId == -1) {
            criteriaVO.setPageNumber(String.valueOf(page));
            criteriaVO.setWithOrderUnit(true);
            criteriaVO.setProductCategoryId(null);
            criteriaVO.setThumbnail(thumbnailCriteria);
            criteriaVO.setWithThumbnail(true);
            criteriaVO.setWithDetail(true);
            criteriaVO.setHasPromotion(true);
        } else {
            criteriaVO.setPageNumber(String.valueOf(page));
            criteriaVO.setWithOrderUnit(true);
            criteriaVO.setThumbnail(thumbnailCriteria);
            criteriaVO.setWithThumbnail(true);
            criteriaVO.setProductCategoryId(String.valueOf(categoryId));
            criteriaVO.setWithDetail(true);
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

    public MutableLiveData<ApiResponse<ProductResponse>> getProductbyBrand(int page, long brandId) {
        ProductCriteria criteriaVO = new ProductCriteria();
        ThumbnailCriteria thumbnailCriteria = new ThumbnailCriteria();
        thumbnailCriteria.setThumbnailType(2);
        OrderUnitCriteria orderUnitCriteria = new OrderUnitCriteria();
        orderUnitCriteria.setWithPromoReward(true);
        criteriaVO.setOrderUnit(orderUnitCriteria);
        criteriaVO.setPageNumber(String.valueOf(page));
        criteriaVO.setWithOrderUnit(true);
        criteriaVO.setProductCategoryId(null);
        criteriaVO.setThumbnail(thumbnailCriteria);
        criteriaVO.setWithThumbnail(true);
        criteriaVO.setWithDetail(true);
        criteriaVO.setBrandId(brandId);
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
        criteriaVO.setWithChildCategoryCount(true);
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

    public MutableLiveData<ApiResponse<List<CategoryVO>>> getSubCategory(Long categoryId) {
        CategoryCriteria criteriaVO = new CategoryCriteria();
        criteriaVO.setType("SUB");
        criteriaVO.setParentCategoryId(categoryId);
        criteriaVO.setWithChildCategoryCount(true);
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

    public MutableLiveData<ApiResponse<List<BrandVO>>> getBrand(Long categoryId) {
        BrandCriteria criteriaVO = new BrandCriteria();
        criteriaVO.setProductCategoryId(categoryId);
        MutableLiveData<ApiResponse<List<BrandVO>>> result = new MutableLiveData<>();
        ApiResponse<List<BrandVO>> apiResponse = new ApiResponse();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getBrand(userAuth, criteriaVO)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<BrandVO> body = response.body();
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

    public MutableLiveData<ApiResponse<List<AdvertisementVO>>> getAdvertisement() {
        AdvertisementCriteria criteria = new AdvertisementCriteria();
        MutableLiveData<ApiResponse<List<AdvertisementVO>>> result = new MutableLiveData<>();
        ApiResponse<List<AdvertisementVO>> apiResponse = new ApiResponse<>();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getAdvertisement(userAuth, criteria)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<AdvertisementVO> body = response.body();
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

    public MutableLiveData<ApiResponse<List<PromoRewardDetailVO>>> getPromoDetail(List<PromoRewardDetailCriteria> criteria) {
        MutableLiveData<ApiResponse<List<PromoRewardDetailVO>>> result = new MutableLiveData<>();
        ApiResponse<List<PromoRewardDetailVO>> apiResponse = new ApiResponse<>();
        IbaAPI api = ServiceGenerator.createService(IbaAPI.class);
        IBAPreferenceManager prefs = new IBAPreferenceManager(getApplication());
        String accessToken = prefs.getAccessToken();
        String userAuth = "Bearer " + accessToken;
        api.getPromoDetails(userAuth, criteria)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    List<PromoRewardDetailVO> body = response.body();
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
