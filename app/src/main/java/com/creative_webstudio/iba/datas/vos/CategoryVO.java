package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class CategoryVO {

    @SerializedName("id")
    private long id;

    @SerializedName("recordRegId")
    private long recordRegId;

    @SerializedName("recordUpdId")
    private long recordUpId;

    @SerializedName("recordRegDate")
    private String recordRegDate;

    @SerializedName("recordUpdDate")
    private String recordUpdDate;

    @SerializedName("parentCategoryId")
    private long parentCategoryId;

    @SerializedName("thumbnailId")
    private int thumbnailId;

    @SerializedName("name")
    private String name;

    @SerializedName("type")
    private String type;

    @SerializedName("childCategoryCount")
    private int childCategoryCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRecordRegId() {
        return recordRegId;
    }

    public void setRecordRegId(long recordRegId) {
        this.recordRegId = recordRegId;
    }

    public long getRecordUpId() {
        return recordUpId;
    }

    public void setRecordUpId(long recordUpId) {
        this.recordUpId = recordUpId;
    }

    public String getRecordRegDate() {
        return recordRegDate;
    }

    public void setRecordRegDate(String recordRegDate) {
        this.recordRegDate = recordRegDate;
    }

    public String getRecordUpdDate() {
        return recordUpdDate;
    }

    public void setRecordUpdDate(String recordUpdDate) {
        this.recordUpdDate = recordUpdDate;
    }

    public long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public int getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(int thumbnailId) {
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

    public int getChildCategoryCount() {
        return childCategoryCount;
    }

    public void setChildCategoryCount(int childCategoryCount) {
        this.childCategoryCount = childCategoryCount;
    }
}
