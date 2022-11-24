package com.example.blog.model;

public abstract class BaseResponse {
    private int status;
    private String error_messages;

    public String getError_messages() {
        return error_messages;
    }

    public void setError_messages(String error_messages) {
        this.error_messages = error_messages;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

}
