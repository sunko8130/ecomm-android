package com.creative_webstudio.iba;

import android.app.Application;

import com.crashlytics.android.Crashlytics;
import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;
import com.github.piasy.biv.BigImageViewer;
import com.github.piasy.biv.loader.glide.GlideImageLoader;

import io.fabric.sdk.android.Fabric;

public class IbaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IbaModel.initAppModel(getApplicationContext());
        Fabric.with(this, new Crashlytics());
        BigImageViewer.initialize(GlideImageLoader.with(getApplicationContext()));
    }
}
