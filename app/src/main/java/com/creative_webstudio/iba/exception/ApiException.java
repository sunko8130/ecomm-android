package com.creative_webstudio.iba.exception;

public class ApiException extends Throwable {
    /**
     * 1 = There is no data in response.
     * 404 = Page not found.
     * 500 = Server error.
     */
    private int errorCode;

    public ApiException() {
        super();
    }

    public ApiException(Throwable t) {
        super(t);
    }

    public ApiException(int errorCode) {
        super();
        this.errorCode = errorCode;
    }

    public ApiException(Throwable t, int errorCode) {
        super(t);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
