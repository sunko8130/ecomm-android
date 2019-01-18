package com.creative_webstudio.iba.datas.vos;

public class CartShowVO {
    private long productId;
    private long unitId;
    private Integer itemQuantity;
    private Long thumbnailId;
    private String productName;
    private String unitShow;
    private long pricePerUnit;
    private int max;
    private int min;

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getUnitId() {
        return unitId;
    }

    public void setUnitId(long unitId) {
        this.unitId = unitId;
    }

    public Integer getItemQuantity() {
        return itemQuantity;
    }

    public void setItemQuantity(Integer itemQuantity) {
        this.itemQuantity = itemQuantity;
    }

    public Long getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(Long thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getUnitShow() {
        return unitShow;
    }

    public void setUnitShow(String unitShow) {
        this.unitShow = unitShow;
    }

    public long getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(long pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
