package com.creative_webstudio.iba.datas.criterias;

public class PromoRewardDetailCriteria {
    private Long orderUnitId;

    private Long promoRewardId;

    private Boolean withOrderUnit;

    public Long getOrderUnitId() {
        return orderUnitId;
    }

    public void setOrderUnitId(Long orderUnitId) {
        this.orderUnitId = orderUnitId;
    }

    public Long getPromoRewardId() {
        return promoRewardId;
    }

    public void setPromoRewardId(Long promoRewardId) {
        this.promoRewardId = promoRewardId;
    }

    public Boolean getWithOrderUnit() {
        return withOrderUnit;
    }

    public void setWithOrderUnit(Boolean withOrderUnit) {
        this.withOrderUnit = withOrderUnit;
    }
}
