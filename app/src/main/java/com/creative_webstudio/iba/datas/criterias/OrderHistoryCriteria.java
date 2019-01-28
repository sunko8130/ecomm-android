package com.creative_webstudio.iba.datas.criterias;

public class OrderHistoryCriteria {

    private int pageNumber;
    private String orderBy;
    private String order;
    private boolean withOrderItem;

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public boolean isWithOrderItem() {
        return withOrderItem;
    }

    public void setWithOrderItem(boolean withOrderItem) {
        this.withOrderItem = withOrderItem;
    }
}
