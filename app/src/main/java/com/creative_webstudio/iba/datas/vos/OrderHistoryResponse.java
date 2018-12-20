package com.creative_webstudio.iba.datas.vos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryResponse {

    @SerializedName("aaData")
    @Expose
    private List<OrderHistoryVO> orderHistoryList = null;
    @SerializedName("iTotalDisplayRecords")
    @Expose
    private Integer iTotalDisplayRecords;

    public List<OrderHistoryVO> getOrderHistoryList() {
        return orderHistoryList;
    }

    public void setOrderHistoryList(List<OrderHistoryVO> orderHistoryList) {
        this.orderHistoryList = orderHistoryList;
    }

    public Integer getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(Integer iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public Integer getITotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setITotalDisplayRecords(Integer iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

}
