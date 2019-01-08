package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class TownshipVO {

    @SerializedName("id")
    private Long id;

    @SerializedName("townshipCode")
    private String code;

    @SerializedName("stateRegionCode")
    private String regionCode;

    @SerializedName("townshipDescEN")
    private String descEN;

    @SerializedName("townshipDescMM")
    private String descMM;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getDescEN() {
        return descEN;
    }

    public void setDescEN(String descEN) {
        this.descEN = descEN;
    }

    public String getDescMM() {
        return descMM;
    }

    public void setDescMM(String descMM) {
        this.descMM = descMM;
    }

    @Override
    public String toString() {
        return getDescEN();
    }
}
