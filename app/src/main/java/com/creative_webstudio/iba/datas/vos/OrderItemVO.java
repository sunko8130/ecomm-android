package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItemVO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("orderId")
    @Expose
    private long orderId;
    @SerializedName("productId")
    @Expose
    private long productId;
    @SerializedName("orderUnitId")
    @Expose
    private long orderUnitId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("orderPrice")
    @Expose
    private long orderPrice;
    @SerializedName("order")
    @Expose
    private long order;

    @SerializedName("product")
    @Expose
    private ProductVO product;

    @SerializedName("orderUnit")
    @Expose
    private OrderUnitVO orderUnit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public long getOrderUnitId() {
        return orderUnitId;
    }

    public void setOrderUnitId(long orderUnitId) {
        this.orderUnitId = orderUnitId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(long orderPrice) {
        this.orderPrice = orderPrice;
    }

    public long getOrder() {
        return order;
    }

    public void setOrder(long order) {
        this.order = order;
    }

    public ProductVO getProduct() {
        return product;
    }

    public void setProduct(ProductVO product) {
        this.product = product;
    }

    public OrderUnitVO getOrderUnit() {
        return orderUnit;
    }

    public void setOrderUnit(OrderUnitVO orderUnit) {
        this.orderUnit = orderUnit;
    }
}
