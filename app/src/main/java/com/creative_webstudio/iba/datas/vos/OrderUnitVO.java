package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class OrderUnitVO {

    @SerializedName("id")
    private long id;

    @SerializedName("recordRegId")
    private long recordRegId;

    @SerializedName("recordUpdId")
    private long recordUpdId;

    @SerializedName("recordRegDate")
    private String recordRegDate;

    @SerializedName("recordUpdDate")
    private String recordUpdDate;

    @SerializedName("productId")
    private long productId;

    @SerializedName("unitName")
    private String unitName;

    @SerializedName("itemName")
    private String itemName;

    @SerializedName("itemsPerUnit")
    private int itemsPerUnit;

    @SerializedName("pricePerUnit")
    private long pricePerUnit;

    @SerializedName("unitInStock")
    private int unitInStock;

    @SerializedName("notifyQuantity")
    private int notifyQuantity;

    @SerializedName("status")
    private String status;

    @SerializedName("minimumOrderQuantity")
    private int minimumOrderQuantity;


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

    public long getRecordUpdId() {
        return recordUpdId;
    }

    public void setRecordUpdId(long recordUpdId) {
        this.recordUpdId = recordUpdId;
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

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
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

    public int getItemsPerUnit() {
        return itemsPerUnit;
    }

    public void setItemsPerUnit(int itemsPerUnit) {
        this.itemsPerUnit = itemsPerUnit;
    }

    public long getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(long pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
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

    public int getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(int unitInStock) {
        this.unitInStock = unitInStock;
    }

    public int getNotifyQuantity() {
        return notifyQuantity;
    }

    public void setNotifyQuantity(int notifyQuantity) {
        this.notifyQuantity = notifyQuantity;
    }
}