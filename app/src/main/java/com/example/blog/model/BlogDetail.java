package com.example.blog.model;

public class BlogDetail extends BaseResponse {
    private BlogRO data;

    public BlogRO getData() {
        return data;
    }

    public void setData(BlogRO data) {
        this.data = data;
    }
}
