package com.creative_webstudio.iba.datas.vos;

public class ProductCriteriaVO {

    private long[] includeIds;
    private boolean withOrderUnits;
    private String pageNumber;
    private String productCategoryId;
    private boolean withThumbnails;
    private int thumbnailType;
    private String word;

    public long[] getIncludeIds() {
        return includeIds;
    }

    public void setIncludeIds(long[] includeIds) {
        this.includeIds = includeIds;
    }

    public boolean isWithOrderUnits() {
        return withOrderUnits;
    }

    public void setWithOrderUnits(boolean withOrderUnits) {
        this.withOrderUnits = withOrderUnits;
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

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
