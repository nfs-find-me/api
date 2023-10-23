package com.findme.api.response;

public class ResponseJson<T> {
    private T data;
    private int statusCode;
    
    private long exp;

    public ResponseJson(T data, int statusCode) {
        this.data = data;
        this.statusCode = statusCode;
    }
    
    public ResponseJson(T data, int statusCode, long exp) {
        this.data = data;
        this.statusCode = statusCode;
        this.exp = exp;
    }

    public T getData() {
        return data;
    }

    public int getStatusCode() {
        return statusCode;
    }
    
    public long getExp() {
        return exp;
    }
}
