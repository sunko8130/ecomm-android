package com.creative_webstudio.iba.networks;

import android.content.Context;

import com.creative_webstudio.iba.utils.AppConstants;
import com.google.gson.Gson;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceGenerator {
    private static Retrofit RETROFIT;

    private ServiceGenerator() {

    }

    private static Retrofit getApiClient() {
        if (RETROFIT == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .build();

            RETROFIT = new Retrofit.Builder()
                    .baseUrl(AppConstants.BaseProductSearchUrl)
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return RETROFIT;
    }

    public static <T> T createService(Class<T> t) {
        return (T) getApiClient().create(t.getClass());
    }
}
