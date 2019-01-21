package com.creative_webstudio.iba.datas.vos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PromoRewardVO {

    @SerializedName("id")
    private Long id;

    @SerializedName("promoId")
    private Long promoId;

    @SerializedName("rewardName")
    private String rewardName;

    @SerializedName("rewardType")
    private String rewardType;

    @SerializedName("discountValue")
    private Double discountValue;

    @SerializedName("promoRewardDetails")
    private List<PromoRewardDetailVO> promoRewardDetailVOList;

    private Integer promoQuantity;

    private String showUnit;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPromoId() {
        return promoId;
    }

    public void setPromoId(Long promoId) {
        this.promoId = promoId;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public Double getDiscountValue() {
        return discountValue;
    }

    public void setDiscountValue(Double discountValue) {
        this.discountValue = discountValue;
    }

    public List<PromoRewardDetailVO> getPromoRewardDetailVOList() {
        return promoRewardDetailVOList;
    }

    public void setPromoRewardDetailVOList(List<PromoRewardDetailVO> promoRewardDetailVOList) {
        this.promoRewardDetailVOList = promoRewardDetailVOList;
    }

    public Integer getPromoQuantity() {
        return promoQuantity;
    }

    public void setPromoQuantity(Integer promoQuantity) {
        this.promoQuantity = promoQuantity;
    }

    public String getShowUnit() {
        return showUnit;
    }

    public void setShowUnit(String showUnit) {
        this.showUnit = showUnit;
    }
}
