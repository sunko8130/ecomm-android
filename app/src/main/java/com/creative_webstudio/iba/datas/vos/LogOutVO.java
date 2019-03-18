package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class LogOutVO {
    @SerializedName("id")
    private String removed_access_token;

    public String getRemoved_access_token() {
        return removed_access_token;
    }

    public void setRemoved_access_token(String removed_access_token) {
        this.removed_access_token = removed_access_token;
    }
}
