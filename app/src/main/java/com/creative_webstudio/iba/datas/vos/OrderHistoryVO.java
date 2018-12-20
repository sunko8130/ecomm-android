package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistoryVO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("recordRegId")
    @Expose
    private Integer recordRegId;
    @SerializedName("recordUpdId")
    @Expose
    private Integer recordUpdId;
    @SerializedName("recordRegDate")
    @Expose
    private String recordRegDate;
    @SerializedName("recordUpdDate")
    @Expose
    private String recordUpdDate;
    @SerializedName("customerId")
    @Expose
    private Integer customerId;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("orderNumber")
    @Expose
    private String orderNumber;
    @SerializedName("orderDate")
    @Expose
    private String orderDate;
    @SerializedName("customer")
    @Expose
    private Object customer;
    @SerializedName("orderItems")
    @Expose
    private List<OrderItemVO> orderItems = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecordRegId() {
        return recordRegId;
    }

    public void setRecordRegId(Integer recordRegId) {
        this.recordRegId = recordRegId;
    }

    public Integer getRecordUpdId() {
        return recordUpdId;
    }

    public void setRecordUpdId(Integer recordUpdId) {
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

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public Object getCustomer() {
        return customer;
    }

    public void setCustomer(Object customer) {
        this.customer = customer;
    }

    public List<OrderItemVO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemVO> orderItems) {
        this.orderItems = orderItems;
    }
}
