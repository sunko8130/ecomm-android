package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderItemVO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("orderId")
    @Expose
    private Long orderId;
    @SerializedName("productId")
    @Expose
    private Long productId;
    @SerializedName("orderUnitId")
    @Expose
    private Long orderUnitId;
    @SerializedName("quantity")
    @Expose
    private int quantity;
    @SerializedName("orderPrice")
    @Expose
    private Double orderPrice;
    @SerializedName("order")
    @Expose
    private Long order;

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

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getOrderUnitId() {
        return orderUnitId;
    }

    public void setOrderUnitId(Long orderUnitId) {
        this.orderUnitId = orderUnitId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Double getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(Double orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Long getOrder() {
        return order;
    }

    public void setOrder(Long order) {
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
