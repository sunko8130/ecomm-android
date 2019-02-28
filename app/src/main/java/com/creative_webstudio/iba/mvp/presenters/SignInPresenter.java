package com.creative_webstudio.iba.mvp.presenters;


import com.creative_webstudio.iba.datas.models.IbaModel;
import com.creative_webstudio.iba.mvp.views.SignInView;


public class SignInPresenter extends BasePresenter<SignInView> {

    @Override
    public void initPresenter(SignInView mView) {
        super.initPresenter(mView);
    }

    public void getToken(String userName, String password){
        IbaModel.getInstance().getTokenByUP(userName,password,mResponseCode);
    }

}
