package com.creative_webstudio.iba.datas.models;

import android.content.Context;

import com.creative_webstudio.iba.networks.IbaAPI;
import com.creative_webstudio.iba.utils.AppConstants;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseModel {
    protected IbaAPI theApiSample;
    protected IbaAPI theApi;
    protected IbaAPI theApiProductSearch;

    protected BaseModel(Context context) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofitSample = new Retrofit.Builder()
                .baseUrl("http://padcmyanmar.com/padc-5/mm-healthcare/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        theApiSample = retrofitSample.create(IbaAPI.class);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_OAUTH_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        theApi = retrofit.create(IbaAPI.class);

        Retrofit retrofitProductSearch = new Retrofit.Builder()
                .baseUrl(AppConstants.BASE_PRODUCT_SEARCH_URL)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        theApiProductSearch = retrofitProductSearch.create(IbaAPI.class);

    }
}
