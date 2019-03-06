package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class BrandVO {

    @SerializedName("id")
    private Long id;

    @SerializedName("thumbnailId")
    private Long thumbnailId;

    @SerializedName("brandName")
    private String brandName;

    @SerializedName("description")
    private String description;

    @SerializedName("productCount")
    private String productCount;

    private boolean isSelected = false;

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

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductCount() {
        return productCount;
    }

    public void setProductCount(String productCount) {
        this.productCount = productCount;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
