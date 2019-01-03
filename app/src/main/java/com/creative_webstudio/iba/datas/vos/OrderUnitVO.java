package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class OrderUnitVO {

    @SerializedName("id")
    private Long id;

    @SerializedName("productId")
    private Long productId;

    @SerializedName("unitName")
    private String unitName;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("itemsPerUnit")
    private Integer itemsPerUnit;

    @SerializedName("pricePerUnit")
    private Long pricePerUnit;

    @SerializedName("unitInStock")
    private Integer unitInStock;

    @SerializedName("notifyQuantity")
    private Integer notifyQuantity;

    @SerializedName("status")
    private String status;

    @SerializedName("minimumOrderQuantity")
    private int minimumOrderQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getItemsPerUnit() {
        return itemsPerUnit;
    }

    public void setItemsPerUnit(Integer itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }

    public Long getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Long pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Integer getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(Integer unitInStock) {
        this.unitInStock = unitInStock;
    }

    public Integer getNotifyQuantity() {
        return notifyQuantity;
    }

    public void setNotifyQuantity(Integer notifyQuantity) {
        this.notifyQuantity = notifyQuantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getMinimumOrderQuantity() {
        return minimumOrderQuantity;
    }

    public void setMinimumOrderQuantity(int minimumOrderQuantity) {
        this.minimumOrderQuantity = minimumOrderQuantity;
    }
}