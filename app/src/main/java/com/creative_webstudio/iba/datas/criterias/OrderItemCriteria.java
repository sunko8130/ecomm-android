package com.creative_webstudio.iba.datas.criterias;

import com.creative_webstudio.iba.datas.vos.ProductVO;

public class OrderItemCriteria {

    private long[] includeIds;
    private boolean withProduct;
    private boolean withOrderUnit;
    private ProductCriteria product;

    public long[] getIncludeIds() {
        return includeIds;
    }

    public void setIncludeIds(long[] includeIds) {
        this.includeIds = includeIds;
    }

    public boolean isWithProduct() {
        return withProduct;
    }

    public void setWithProduct(boolean withProduct) {
        this.withProduct = withProduct;
    }

    public boolean isWithOrderUnit() {
        return withOrderUnit;
    }

    public void setWithOrderUnit(boolean withOrderUnit) {
        this.withOrderUnit = withOrderUnit;
    }

    public ProductCriteria getProduct() {
        return product;
    }

    public void setProduct(ProductCriteria product) {
        this.product = product;
    }
}
