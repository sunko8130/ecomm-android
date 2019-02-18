package com.creative_webstudio.iba.datas.criterias;

public class CategoryCriteria {

    private String type;

    private Long parentCategoryId;

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
}
