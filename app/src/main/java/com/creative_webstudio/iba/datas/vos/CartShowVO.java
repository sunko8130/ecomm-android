package com.creative_webstudio.iba.datas.vos;

public class CartShowVO {
    private Long productId;
    private Long unitId;
    private Integer itemQuantity;
    private Long thumbnailId;
    private String productName;
    private String unitShow;
    private Double pricePerUnit;
    private int max;
    private int min;
    private Double promoAmount;
    private String promoItem;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
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

    public Double getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Double pricePerUnit) {
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

    public Double getPromoAmount() {
        return promoAmount;
    }

    public void setPromoAmount(Double promoAmount) {
        this.promoAmount = promoAmount;
    }

    public String getPromoItem() {
        return promoItem;
    }

    public void setPromoItem(String promoItem) {
        this.promoItem = promoItem;
    }
}
