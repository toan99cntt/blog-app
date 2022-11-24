package com.example.blog.model;

import java.util.List;

public class Message extends BaseResponse{
    protected List<MessageRO> data;

    public List<MessageRO> getData() {
        return data;
    }

    public void setData(List<MessageRO> data) {
        this.data = data;
    }
}
