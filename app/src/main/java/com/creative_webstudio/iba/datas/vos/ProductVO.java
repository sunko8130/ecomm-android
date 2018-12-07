package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ProductVO {

    @SerializedName("id")
    private long id;

    @SerializedName("recordRegId")
    private int recordRegId;

    @SerializedName("recordUpdId")
    private int recordUpdId;

    @SerializedName("recordRegDate")
    private String recordRegDate;

    @SerializedName("recordUpdDate")
    private String recordUpdDate;

    @SerializedName("productCategoryId")
    private int productCategoryId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("unitInStock")
    private int unitInStock;

    @SerializedName("description")
    private String description;

    @SerializedName("productDetails")
    private ProductDetailsVo productDetailsVo;

    @SerializedName("thumbnailIds")
    private List<Integer> thumbnailIdsList;

    @SerializedName("thumbnails")
    private ThumbnailsVo thumbnails;

    @SerializedName("orderUnits")
    private List<OrderUnitsVo> orderUnits;

    @SerializedName("productCategory")
    private ProductCategoryVo productCategory;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getRecordRegId() {
        return recordRegId;
    }

    public void setRecordRegId(int recordRegId) {
        this.recordRegId = recordRegId;
    }

    public int getRecordUpdId() {
        return recordUpdId;
    }

    public void setRecordUpdId(int recordUpdId) {
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

    public int getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(int productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(int unitInStock) {
        this.unitInStock = unitInStock;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getThumbnailIdsList() {
        return thumbnailIdsList;
    }

    public void setThumbnailIdsList(List<Integer> thumbnailIdsList) {
        this.thumbnailIdsList = thumbnailIdsList;
    }

    public ProductDetailsVo getProductDetailsVo() {
        return productDetailsVo;
    }

    public void setProductDetailsVo(ProductDetailsVo productDetailsVo) {
        this.productDetailsVo = productDetailsVo;
    }

    public ThumbnailsVo getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ThumbnailsVo thumbnails) {
        this.thumbnails = thumbnails;
    }

    public List<OrderUnitsVo> getOrderUnits() {
        return orderUnits;
    }

    public void setOrderUnits(List<OrderUnitsVo> orderUnits) {
        this.orderUnits = orderUnits;
    }

    public ProductCategoryVo getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryVo productCategory) {
        this.productCategory = productCategory;
    }
}


