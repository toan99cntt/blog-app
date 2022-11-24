package com.example.blog.model;

import java.util.List;

public class UpdateStatusNotify extends BaseResponse {
    private NotificationRO data;

    public NotificationRO getData() {
        return data;
    }

    public void setData(NotificationRO data) {
        this.data = data;
    }
}
