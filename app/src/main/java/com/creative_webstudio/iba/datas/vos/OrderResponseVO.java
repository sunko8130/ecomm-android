package com.creative_webstudio.iba.datas.vos;


import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderResponseVO {
    private List<CartVO> responseData;

    public List<CartVO> getResponseData() {
        return responseData;
    }

    public void setResponseData(List<CartVO> responseData) {
        this.responseData = responseData;
    }
}
