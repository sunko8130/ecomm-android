package com.creative_webstudio.iba.datas.vos;

public class CartVO {
    private Long productId;
    private Long orderUnitId;
    private int quantity;
    private int unitInStock;

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

    public int getUnitInStock() {
        return unitInStock;
    }

    public void setUnitInStock(int unitInStock) {
        this.unitInStock = unitInStock;
    }
}
