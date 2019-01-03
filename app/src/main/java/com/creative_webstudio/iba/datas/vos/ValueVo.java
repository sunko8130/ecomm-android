package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class ValueVo {

    @SerializedName("index")
    private Integer index;

    @SerializedName("title")
    private String title;

    @SerializedName("description")
    private String description;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
