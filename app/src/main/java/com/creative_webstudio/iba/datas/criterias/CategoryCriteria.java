package com.creative_webstudio.iba.datas.criterias;

public class CategoryCriteria {

    private String type;

    private Long parentCategoryId;

    private Boolean withChildCategoryCount;

    public Long getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(Long parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean isWithChildCategoryCount() {
        return withChildCategoryCount;
    }

    public void setWithChildCategoryCount(Boolean withChildCategoryCount) {
        this.withChildCategoryCount = withChildCategoryCount;
    }
}
