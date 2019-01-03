package com.creative_webstudio.iba.networks;

import com.creative_webstudio.iba.datas.vos.CartVO;
import com.creative_webstudio.iba.datas.criterias.CategoryCriteria;
import com.creative_webstudio.iba.datas.vos.CategoryVO;
import com.creative_webstudio.iba.datas.criterias.OrderHistoryCriteria;
import com.creative_webstudio.iba.datas.vos.CustomerVO;
import com.creative_webstudio.iba.datas.vos.OrderHistoryResponse;
import com.creative_webstudio.iba.datas.criterias.OrderItemCriteria;
import com.creative_webstudio.iba.datas.vos.OrderItemVO;
import com.creative_webstudio.iba.datas.criterias.ProductCriteria;
import com.creative_webstudio.iba.datas.vos.ProductResponse;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;

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
//            @Body Criteria criteriaVO);

    @POST("product/search/paging")
    Observable<Response<ProductResponse>> getProduct(@Header("Authorization") String authHeader,
                                                     @Body ProductCriteria criteriaVO);

    @POST("product/search/paging")
    Observable<Response<ProductResponse>> getProductById(@Header("Authorization") String authHeader,
                                                         @Body ProductCriteria criteriaVO);

//    @POST("order_unit/search/list")
//    Observable<Response<List<OrderUnitVO>>> getOrderUnit(@Header("Authorization") String authHeader,
//                                                         @Body Criteria criteriaVO);

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

    @GET("customer/me")
    Observable<Response<CustomerVO>> getCustomerInfo(@Header("Authorization") String authHeader);

}
