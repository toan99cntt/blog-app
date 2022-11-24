package com.example.blog.model;

import java.util.List;

public class Member extends BaseResponse {
    private List<MemberRO> data;

    public List<MemberRO> getData() {
        return data;
    }

    public void setData(List<MemberRO> data) {
        this.data = data;
    }
}
