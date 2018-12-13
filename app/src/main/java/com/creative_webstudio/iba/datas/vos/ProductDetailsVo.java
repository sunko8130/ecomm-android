package com.creative_webstudio.iba.datas.vos;


import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class ProductDetailsVo {

    @SerializedName("value")
    private Map<String, String> valueList;

    @SerializedName("empty")
    private Boolean empty;

    @SerializedName("present")
    private Boolean present;

    @SerializedName("notEmpty")
    private Boolean notEmpty;

    @SerializedName("notPresent")
    private Boolean notPresent;


}
