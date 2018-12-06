package com.creative_webstudio.iba.datas.vos;

public class CriteriaVo {

    private String word;
    private int offset;
    private int limit;

    public CriteriaVo(String word, int offset, int limit) {
        this.word = word;
        this.offset = offset;
        this.limit = limit;
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
}
