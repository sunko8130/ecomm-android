package com.creative_webstudio.iba.datas.vos;


import com.google.gson.annotations.SerializedName;

import java.util.List;

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


}
