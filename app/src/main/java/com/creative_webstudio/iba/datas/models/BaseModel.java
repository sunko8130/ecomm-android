package com.creative_webstudio.iba.datas.models;

import android.content.Context;

import com.creative_webstudio.iba.networks.IbaAPI;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseModel {
    protected IbaAPI theApiSample;
    protected IbaAPI theApi;

    protected BaseModel(Context context){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();

        Retrofit retrofitSample=new Retrofit.Builder()
                .baseUrl("http://padcmyanmar.com/padc-5/mm-healthcare/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        theApiSample =retrofitSample.create(IbaAPI.class);

        Retrofit retrofit=new Retrofit.Builder()
                .baseUrl("http://192.168.100.100:8281/iba-uae/")
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        theApi=retrofit.create(IbaAPI.class);
    }
}
