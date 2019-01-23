package com.creative_webstudio.iba.datas.criterias;

public class ProductCriteria {

    private long[] includeIds;
    private boolean withOrderUnit;
    private String pageNumber;
    private String productCategoryId;
    private boolean withThumbnail;
    private String word;
    private ThumbnailCriteria thumbnail;
    private boolean withDetail;
    private OrderUnitCriteria orderUnit;
    private Boolean hasPromotion=false;

    public long[] getIncludeIds() {
        return includeIds;
    }

    public void setIncludeIds(long[] includeIds) {
        this.includeIds = includeIds;
    }

    public boolean isWithOrderUnit() {
        return withOrderUnit;
    }

    public void setWithOrderUnit(boolean withOrderUnit) {
        this.withOrderUnit = withOrderUnit;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(String productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public boolean isWithThumbnail() {
        return withThumbnail;
    }

    public void setWithThumbnail(boolean withThumbnail) {
        this.withThumbnail = withThumbnail;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public ThumbnailCriteria getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(ThumbnailCriteria thumbnail) {
        this.thumbnail = thumbnail;
    }

    public boolean isWithDetail() {
        return withDetail;
    }

    public void setWithDetail(boolean withDetail) {
        this.withDetail = withDetail;
    }

    public OrderUnitCriteria getOrderUnit() {
        return orderUnit;
    }

    public void setOrderUnit(OrderUnitCriteria orderUnit) {
        this.orderUnit = orderUnit;
    }

    public Boolean getHasPromotion() {
        return hasPromotion;
    }

    public void setHasPromotion(Boolean hasPromotion) {
        this.hasPromotion = hasPromotion;
    }
}
