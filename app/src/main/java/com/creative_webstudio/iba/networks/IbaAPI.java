package com.creative_webstudio.iba.networks;

import com.creative_webstudio.iba.datas.criterias.AdvertisementCriteria;
import com.creative_webstudio.iba.datas.criterias.ConfigurationCriteria;
import com.creative_webstudio.iba.datas.criterias.CustomerCriteria;
import com.creative_webstudio.iba.datas.criterias.PromoRewardDetailCriteria;
import com.creative_webstudio.iba.datas.vos.AdvertisementVO;
import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.criterias.CategoryCriteria;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.datas.criterias.OrderHistoryCriteria;
import com.creative_webstudio.iba.datas.vos.ConfigurationVO;
import com.creative_webstudio.iba.datas.vos.CustomerVO;
import com.creative_webstudio.iba.datas.vos.OrderHistoryResponse;
import com.creative_webstudio.iba.datas.criterias.OrderItemCriteria;
import com.creative_webstudio.iba.datas.vos.OrderItemVO;
import com.creative_webstudio.iba.datas.criterias.ProductCriteria;
import com.creative_webstudio.iba.datas.vos.ProductResponse;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.PromoRewardDetailVO;
import com.creative_webstudio.iba.datas.vos.RegionVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.creative_webstudio.iba.datas.vos.TownshipVO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface IbaAPI {

    @FormUrlEncoded
    @POST("GetHealthcareInfo.php")
    Observable<HCInfoResponse> loadHCInfo(
            @Field("access_token") String accessToken);

    @FormUrlEncoded
    @POST("oauth/token")
    Observable<Response<TokenVO>> getTokenByUP(
            @Header("Authorization") String authHeader,
            @Field("username") String username,
            @Field("password") String password,
            @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("oauth/token")
    Observable<Response<TokenVO>> getTokenByRefresh(
            @Header("Authorization") String authHeader,
            @Field("refresh_token") String refreshToken,
            @Field("grant_type") String grantType);

    @POST("product/search/list")
    Observable<Response<List<ProductVO>>> getProductSearch(
            @Header("Authorization") String authHeader,
            @Body ProductCriteria criteriaVO);

//    @POST("product/search/paging")
//    Observable<Response<ProductResponse>> getProductPaging(
//            @Header("Authorization") String authHeader,
//            @Body ConfigurationCriteria criteriaVO);

    @POST("product/search/paging")
    Observable<Response<ProductResponse>> getProduct(@Header("Authorization") String authHeader,
                                                     @Body ProductCriteria criteriaVO);

    @POST("product/search/paging")
    Observable<Response<ProductResponse>> getProductById(@Header("Authorization") String authHeader,
                                                         @Body ProductCriteria criteriaVO);

//    @POST("order_unit/search/list")
//    Observable<Response<List<OrderUnitVO>>> getOrderUnit(@Header("Authorization") String authHeader,
//                                                         @Body ConfigurationCriteria criteriaVO);

    @POST("advertisement/search/list")
    Observable<Response<List<AdvertisementVO>>> getAdvertisement(@Header("Authorization") String authHeader,
                                                                 @Body AdvertisementCriteria criteriaVO);

    @POST("promo_reward_detail/search/applied_rewards")
    Observable<Response<List<PromoRewardDetailVO>>> getPromoDetails(@Header("Authorization") String authHeader,
                                                                     @Body List<PromoRewardDetailCriteria> criteriaVO);

    @POST("product_category/search/list")
    Observable<Response<List<CategoryVO>>> getCategory(@Header("Authorization") String authHeader,
                                                       @Body CategoryCriteria criteriaVO);

    @POST("order/add")
    Observable<Response<Integer>> sendOrder(@Header("Authorization") String authHeader,
                                            @Body List<CartVO> criteriaVO);

    @POST("order/search/paging")
    Observable<Response<OrderHistoryResponse>> getOrderHistory(@Header("Authorization") String authHeader,
                                                               @Body OrderHistoryCriteria criteriaVO);

    @POST("order_item/search/list")
    Observable<Response<List<OrderItemVO>>> getOrderItems(@Header("Authorization") String authHeader,
                                                          @Body OrderItemCriteria criteriaVO);

    @FormUrlEncoded
    @PUT("order/update/status")
    Observable<Response<Integer>> updateOrder(@Header("Authorization") String authHeader,
                                              @Field("orderId") long orderId,
                                              @Field("status") String status);

    @FormUrlEncoded
    @PUT("customer/me/change_password")
    Observable<Response<Integer>> updatePassword(@Header("Authorization") String authHeader,
                                              @Field("oldPassword") String oldPassword,
                                              @Field("newPassword") String newPassword);

    @GET("customer/me")
    Observable<Response<CustomerVO>> getCustomerInfo(@Header("Authorization") String authHeader);

    @POST("customer/me")
    Observable<Response<ConfigurationVO>> getConfigurationInfo(@Header("Authorization") String authHeader,
                                                               @Body ConfigurationCriteria configurationCriteria);

    @PUT("customer/me")
    Observable<Response<Integer>> updateCustomerInfo(@Header("Authorization") String authHeader,
                                                     @Body CustomerCriteria criteriaVO);

    @GET("mm_map/state_region")
    Observable<Response<List<RegionVO>>> getRegion(@Header("Authorization") String authHeader);


    @GET("mm_map/township")
    Observable<Response<List<TownshipVO>>> getTownShip(@Header("Authorization") String authHeader,
                                                       @Query("stateRegionCode") String regionCode);

}
