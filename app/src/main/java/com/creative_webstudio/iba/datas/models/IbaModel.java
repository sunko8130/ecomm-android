package com.creative_webstudio.iba.datas.models;

import android.content.Context;

public class IbaModel extends BaseModel {
    public static IbaModel objInstance;

    protected IbaModel(Context context) {
        super(context);
    }
    public static void initAppModel(Context context) {
        objInstance = new IbaModel(context);
    }

    public static IbaModel getInstance() {
        if (objInstance == null) {
            throw new RuntimeException("IbaModel is being invoked before initializing.");
        }
        return objInstance;
    }
}
