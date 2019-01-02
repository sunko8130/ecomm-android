package com.creative_webstudio.iba.datas.vos;

public class CartVO {
    private long productId;
    private long orderUnitId;
    private int quantity;
    private int unitInStock;

    public CartVO() {
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

    public int getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(int unitInStock) {
        this.unitInStock = unitInStock;
    }
}
