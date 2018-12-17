package com.creative_webstudio.iba.datas.vos;

public class CriteriaVO {

    private String word;
    private int offset;
    private int limit;
    private int pageNumber;
    private long productId;
    private boolean withOrderUnits;

    public CriteriaVO() {
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public boolean isWithOrderUnits() {
        return withOrderUnits;
    }

    public void setWithOrderUnits(boolean withOrderUnits) {
        this.withOrderUnits = withOrderUnits;
    }
}
