package com.creative_webstudio.iba.networks;

import com.creative_webstudio.iba.datas.vos.CriteriaVo;
import com.creative_webstudio.iba.datas.vos.ProductPagingVO;
import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.datas.vos.TokenVO;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

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
            @Body CriteriaVo criteriaVo);

    @POST("product/search/paging")
    Observable<Response<ProductPagingVO>> getProductPaging(
            @Header("Authorization") String authHeader,
            @Body CriteriaVo criteriaVo);


}
