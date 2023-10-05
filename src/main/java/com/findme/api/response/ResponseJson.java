package com.findme.api.response;

public class ResponseJson<T> {
    private T data;
    private int statusCode;

    public ResponseJson(T data, int statusCode) {
        this.data = data;
        this.statusCode = statusCode;
    }

    public T getData() {
        return data;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
