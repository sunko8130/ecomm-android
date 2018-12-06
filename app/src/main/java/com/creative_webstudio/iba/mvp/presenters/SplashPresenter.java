package com.creative_webstudio.iba.mvp.presenters;

import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.mvp.views.SplashView;

public class SplashPresenter extends BasePresenter<SplashView> {
    @Override
    public void initPresenter(SplashView mView) {
        super.initPresenter(mView);
    }

    public void getAccess(String token){
        IbaModel.getInstance().getTokenbyRefresh(token,mResponseCode);
    }
}
