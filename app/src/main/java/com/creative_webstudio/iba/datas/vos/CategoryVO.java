package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class CategoryVO {

    @SerializedName("id")
    private Long id;

    @SerializedName("parentCategoryId")
    private Long parentCategoryId;

    @SerializedName("thumbnailId")
    private Integer thumbnailId;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("childCategoryCount")
    private Integer childCategoryCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public Integer getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(Integer thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getChildCategoryCount() {
        return childCategoryCount;
    }

    public void setChildCategoryCount(Integer childCategoryCount) {
        this.childCategoryCount = childCategoryCount;
    }
}
