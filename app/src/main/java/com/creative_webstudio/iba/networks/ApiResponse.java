package com.creative_webstudio.iba.networks;

public class ApiResponse<T> {
    private Throwable apiException = null;
    private T data = null;

    public Throwable getApiException() {
        return apiException;
    }

    public void setApiException(Throwable apiException) {
        this.apiException = apiException;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
