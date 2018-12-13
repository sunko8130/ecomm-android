package com.creative_webstudio.iba;

import android.app.Application;

import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.utils.IBAPreferenceManager;

public class IbaApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        IbaModel.initAppModel(getApplicationContext());
    }
}
