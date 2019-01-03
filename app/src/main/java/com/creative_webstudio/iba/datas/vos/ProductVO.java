package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductVO {

    @SerializedName("id")
    private Long id;

    @SerializedName("productCategoryId")
    private Integer productCategoryId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("description")
    private String description;

    @SerializedName("status")
    private String status;

    @SerializedName("productDetails")
    private ProductDetailsVo productDetailsVo;

    @SerializedName("thumbnailIds")
    private List<Integer> thumbnailIdsList;

    @SerializedName("thumbnails")
    private ThumbnailsVo thumbnails;

    @SerializedName("orderUnits")
    private List<OrderUnitVO> orderUnits;

    @SerializedName("productCategory")
    private ProductCategoryVo productCategory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProductCategoryId() {
        return productCategoryId;
    }

    public void setProductCategoryId(Integer productCategoryId) {
        this.productCategoryId = productCategoryId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProductDetailsVo getProductDetailsVo() {
        return productDetailsVo;
    }

    public void setProductDetailsVo(ProductDetailsVo productDetailsVo) {
        this.productDetailsVo = productDetailsVo;
    }

    public List<Integer> getThumbnailIdsList() {
        return thumbnailIdsList;
    }

    public void setThumbnailIdsList(List<Integer> thumbnailIdsList) {
        this.thumbnailIdsList = thumbnailIdsList;
    }

    public ThumbnailsVo getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(ThumbnailsVo thumbnails) {
        this.thumbnails = thumbnails;
    }

    public List<OrderUnitVO> getOrderUnits() {
        return orderUnits;
    }

    public void setOrderUnits(List<OrderUnitVO> orderUnits) {
        this.orderUnits = orderUnits;
    }

    public ProductCategoryVo getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategoryVo productCategory) {
        this.productCategory = productCategory;
    }
}


