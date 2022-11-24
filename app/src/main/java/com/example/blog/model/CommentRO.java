package com.example.blog.model;

public class CommentRO{
    protected int id;
    protected String content;
    protected int like_count;
    protected String created_at;
    protected UserRO member;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }

    public UserRO getMember() {
        return member;
    }

    public void setMember(UserRO member) {
        this.member = member;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
