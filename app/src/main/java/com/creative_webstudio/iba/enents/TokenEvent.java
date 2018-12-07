package com.creative_webstudio.iba.enents;

public class TokenEvent {
    private Integer responseCode;

    public TokenEvent(Integer responseCode) {
        this.responseCode = responseCode;
    }

    public Integer getResponseCode() {
        return responseCode;
    }
}
