package com.creative_webstudio.iba;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.creative_webstudio.iba.datas.models.IbaModel;

import io.fabric.sdk.android.Fabric;

public class IbaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IbaModel.initAppModel(getApplicationContext());
        Fabric.with(this, new Crashlytics());

    }
}
