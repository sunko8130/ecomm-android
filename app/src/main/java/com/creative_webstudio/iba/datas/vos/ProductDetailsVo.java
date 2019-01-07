package com.creative_webstudio.iba.datas.vos;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ProductDetailsVo {

    @SerializedName("value")
    private List<ValueVo> valueList;

    @SerializedName("empty")
    private Boolean empty;

    @SerializedName("present")
    private Boolean present;

    @SerializedName("notEmpty")
    private Boolean notEmpty;

    @SerializedName("notPresent")
    private Boolean notPresent;

    public List<ValueVo> getValueList() {
        return valueList;
    }

    public void setValueList(List<ValueVo> valueList) {
        this.valueList = valueList;
    }

    public Boolean getEmpty() {
        return empty;
    }

    public void setEmpty(Boolean empty) {
        this.empty = empty;
    }

    public Boolean getPresent() {
        return present;
    }

    public void setPresent(Boolean present) {
        this.present = present;
    }

    public Boolean getNotEmpty() {
        return notEmpty;
    }

    public void setNotEmpty(Boolean notEmpty) {
        this.notEmpty = notEmpty;
    }

    public Boolean getNotPresent() {
        return notPresent;
    }

    public void setNotPresent(Boolean notPresent) {
        this.notPresent = notPresent;
    }
}
