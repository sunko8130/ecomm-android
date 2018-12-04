package com.creative_webstudio.iba.networks;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface IbaAPI {

    @FormUrlEncoded
    @POST("GetHealthcareInfo.php")
    Observable<HCInfoResponse> loadHCInfo(
            @Field("access_token") String accessToken);
//    fun loadHCInfo(
//            @Field("access_token") accessToken: String) : Observable<HCInfoResponse>
}
