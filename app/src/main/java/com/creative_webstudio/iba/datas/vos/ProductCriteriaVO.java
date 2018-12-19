package com.creative_webstudio.iba.datas.vos;

public class ProductCriteriaVO extends BaseCriteriaVO{


    private long[] productId;
    private boolean withOrderUnits;
    private int pageNumber;
    private boolean withThumbnails;
    private int thumbnailType;

    public long[] getProductId() {
        return productId;
    }

    public void setProductId(long[] productId) {
        this.productId = productId;
    }

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
