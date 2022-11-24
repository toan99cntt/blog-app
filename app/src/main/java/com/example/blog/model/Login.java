package com.example.blog.model;

public class Login {
    private int status;
    private LoginRO data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public LoginRO getData() {
        return data;
    }

    public void setData(LoginRO data) {
        this.data = data;
    }
}
