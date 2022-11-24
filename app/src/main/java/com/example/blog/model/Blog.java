package com.example.blog.model;

import java.util.List;

public class Blog extends BaseResponse {
    private List<BlogRO> data;

    public List<BlogRO> getData() {
        return data;
    }

    public void setData(List<BlogRO> data) {
        this.data = data;
    }
}
