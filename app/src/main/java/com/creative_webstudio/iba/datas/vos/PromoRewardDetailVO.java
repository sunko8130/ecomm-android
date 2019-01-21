package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

public class PromoRewardDetailVO {

    @SerializedName("id")
    private Long id;

    @SerializedName("promoRewardId")
    private Long promoRewardId;

    @SerializedName("orderUnitId")
    private Long orderUnitId;

    @SerializedName("orderQuantity")
    private Integer orderQuantity;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPromoRewardId() {
        return promoRewardId;
    }

    public void setPromoRewardId(Long promoRewardId) {
        this.promoRewardId = promoRewardId;
    }

    public Long getOrderUnitId() {
        return orderUnitId;
    }

    public void setOrderUnitId(Long orderUnitId) {
        this.orderUnitId = orderUnitId;
    }

    public Integer getOrderQuantity() {
        return orderQuantity;
    }

    public void setOrderQuantity(Integer orderQuantity) {
        this.orderQuantity = orderQuantity;
    }
}
