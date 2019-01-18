package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AdvertisementVO {
    @SerializedName("id")
    @Expose
    private Long id;

    @SerializedName("thumbnailId")
    @Expose
    private Long thumbnailId;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("advOrder")
    @Expose
    private Integer advOrder;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAdvOrder() {
        return advOrder;
    }

    public void setAdvOrder(Integer advOrder) {
        this.advOrder = advOrder;
    }
}
