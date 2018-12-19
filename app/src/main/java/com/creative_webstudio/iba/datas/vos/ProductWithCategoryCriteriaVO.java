package com.creative_webstudio.iba.datas.vos;

public class ProductWithCategoryCriteriaVO{

    private boolean withOrderUnits;
    private int pageNumber;
    private String productCategoryId;
    private boolean withThumbnails;
    private int thumbnailType;

    public boolean isWithOrderUnits() {
        return withOrderUnits;
    }

    public void setWithOrderUnits(boolean withOrderUnits) {
        this.withOrderUnits = withOrderUnits;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
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
}
