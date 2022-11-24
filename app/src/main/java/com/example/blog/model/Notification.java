package com.example.blog.model;

import java.util.List;

public class Notification extends BaseResponse {
    private List<NotificationRO> data;

    public List<NotificationRO> getData() {
        return data;
    }

    public void setData(List<NotificationRO> data) {
        this.data = data;
    }
}
