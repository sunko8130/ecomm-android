package com.creative_webstudio.iba.datas;

import com.creative_webstudio.iba.datas.vos.ProductVO;
import com.creative_webstudio.iba.exception.ApiException;

import java.util.List;

import retrofit2.Response;

public class ApiResponse<T> {
    private T data;
    private Throwable error;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Throwable getError() {
        return error;
    }

    public void setError(Throwable error) {
        this.error = error;
    }
}
