package com.creative_webstudio.iba.datas.vos;

public class SpinnerVO {
    private String unitName;
    private int itemPerUnit;
    private String itemName;

    public SpinnerVO(String unitName, int getItemsPerUnit, String itemName) {
        this.unitName = unitName;
        this.itemPerUnit = getItemsPerUnit;
        this.itemName = itemName;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public int getItemPerUnit() {
        return itemPerUnit;
    }

    public void setItemPerUnit(int itemPerUnit) {
        this.itemPerUnit = itemPerUnit;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    @Override
    public String toString() {
        return "1"+getUnitName()+" ("+getItemPerUnit()+" "+getItemName()+")";
    }
}
