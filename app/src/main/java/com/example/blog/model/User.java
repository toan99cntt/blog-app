package com.example.blog.model;

public class User {
    private int status;
    private String error_messages;
    private UserRO data;

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

    public UserRO getData() {
        return data;
    }

    public void setData(UserRO data) {
        this.data = data;
    }
}
