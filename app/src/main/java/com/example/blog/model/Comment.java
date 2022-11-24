package com.example.blog.model;

public class Comment extends BaseResponse {
    protected CommentRO data;

    public CommentRO getData() {
        return data;
    }

    public void setData(CommentRO data) {
        this.data = data;
    }
}
