package com.creative_webstudio.iba.networks;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class HCInfoResponse {
    @SerializedName("code")
    private int code=0;

    @SerializedName("message")
    private String message="";



    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }



}
