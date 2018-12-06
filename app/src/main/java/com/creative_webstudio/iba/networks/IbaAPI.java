package com.creative_webstudio.iba.networks;

import com.creative_webstudio.iba.datas.vos.TokenVO;

import io.reactivex.Observable;
import retrofit2.Response;
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
    Observable<Response<TokenVO>> getTokenbyUP(
            @Header("Authorization") String authHeader,
            @Field("username") String accessToken,
            @Field("password") String password,
            @Field("grant_type") String grantType);

    @FormUrlEncoded
    @POST("oauth/token")
    Observable<Response<TokenVO>> getTokenbyRefresh(
            @Header("Authorization") String authHeader,
            @Field("refresh_token") String refreshToken,
            @Field("grant_type") String grantType);
}
