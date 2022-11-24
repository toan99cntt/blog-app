package com.example.blog.model;

import java.util.List;

public class NotificationRO {
    protected Object data;
    protected int id;
    protected int blog_id;
    protected String content;
    protected String created_at;
    protected int has_seen;

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getHas_seen() {
        return has_seen;
    }

    public void setHas_seen(int has_seen) {
        this.has_seen = has_seen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBlog_id() {
        return blog_id;
    }

    public void setBlog_id(int blog_id) {
        this.blog_id = blog_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
