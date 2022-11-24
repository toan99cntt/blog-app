package com.example.blog.model;

import java.util.List;

public class MemberRO {
    private int id;
    private String name;
    private String email;
    private String created_at;
    private Image[] avatar;

    public Image[] getAvatar() {
        return avatar;
    }

    public void setAvatar(Image[] avatar) {
        this.avatar = avatar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
