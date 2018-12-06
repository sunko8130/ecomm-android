package com.creative_webstudio.iba.networks.responses;

import com.creative_webstudio.iba.datas.vos.TokenVO;
import com.google.gson.annotations.SerializedName;

public class TokenResponse {

    private int code;

    private String status;

    private TokenVO tokenVo;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
