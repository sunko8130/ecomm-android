package com.creative_webstudio.iba.datas.vos;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderHistoryVO {

    @SerializedName("aaData")
    @Expose
    private List<AaDatum> aaData = null;
    @SerializedName("iTotalDisplayRecords")
    @Expose
    private Integer iTotalDisplayRecords;

    public List<AaDatum> getAaData() {
        return aaData;
    }

    public void setAaData(List<AaDatum> aaData) {
        this.aaData = aaData;
    }

    public Integer getITotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setITotalDisplayRecords(Integer iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public class AaDatum {

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
        private List<Object> orderItems = null;

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

        public List<Object> getOrderItems() {
            return orderItems;
        }

        public void setOrderItems(List<Object> orderItems) {
            this.orderItems = orderItems;
        }

    }
}
