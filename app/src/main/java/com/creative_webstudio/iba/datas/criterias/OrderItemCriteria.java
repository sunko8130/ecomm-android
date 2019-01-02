package com.creative_webstudio.iba.datas.criterias;

public class OrderItemCriteria {

    private long[] includeIds;
    private boolean withProduct;
    private boolean withThumbnails;
    private int thumbnailType;
    private boolean withOrderUnit;

    public long[] getIncludeIds() {
        return includeIds;
    }

    public void setIncludeIds(long[] includeIds) {
        this.includeIds = includeIds;
    }

    public boolean isWithProduct() {
        return withProduct;
    }

    public void setWithProduct(boolean withProduct) {
        this.withProduct = withProduct;
    }

    public boolean isWithThumbnails() {
        return withThumbnails;
    }

    public void setWithThumbnails(boolean withThumbnails) {
        this.withThumbnails = withThumbnails;
    }

    public int getThumbnailType() {
        return thumbnailType;
    }

    public void setThumbnailType(int thumbnailType) {
        this.thumbnailType = thumbnailType;
    }

    public boolean isWithOrderUnit() {
        return withOrderUnit;
    }

    public void setWithOrderUnit(boolean withOrderUnit) {
        this.withOrderUnit = withOrderUnit;
    }
}
