package com.example.blog.model;

public class MessageDetail extends BaseResponse {
    protected MessageRO data;

    public MessageRO getData() {
        return data;
    }

    public void setData(MessageRO data) {
        this.data = data;
    }
}
