package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ProductPagingVO {
    @SerializedName("aaData")
    private List<ProductVO> productVOList;

    @SerializedName("iTotalDisplayRecords")
    private int iTotalDisplayRecords;

    public List<ProductVO> getProductVOList() {
        return productVOList;
    }

    public void setProductVOList(List<ProductVO> productVOList) {
        this.productVOList = productVOList;
    }

    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }
}
