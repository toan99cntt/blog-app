package com.example.blog.model;

import java.util.List;

public class BlogRO {
    protected int id;
    protected String title;
    protected String content;
    protected String[] _title;
    protected String[] _content;
    protected List<CommentRO> comments;
    protected int like_count;
    protected int view_count;
    protected int member_id;
    protected String created_at;
    protected Image[] image;
    protected MemberRO member;
    protected int status;
    private boolean likes;

    public boolean isLikes() {
        return likes;
    }

    public void setLikes(boolean likes) {
        this.likes = likes;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Image[] getImage() {
        return image;
    }

    public void setImage(Image[] image) {
        this.image = image;
    }

    public int getView_count() {
        return view_count;
    }

    public void setView_count(int view_count) {
        this.view_count = view_count;
    }

    public List<CommentRO> getComments() {
        return comments;
    }

    public void setComments(List<CommentRO> comments) {
        this.comments = comments;
    }

    public String[] get_title() {
        return _title;
    }

    public void set_title(String[] _title) {
        this._title = _title;
    }

    public String[] get_content() {
        return _content;
    }

    public void set_content(String[] _content) {
        this._content = _content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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

    public int getMember_id() {
        return member_id;
    }

    public void setMember_id(int member_id) {
        this.member_id = member_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public MemberRO getMember() {
        return member;
    }

    public void setMember(MemberRO member) {
        this.member = member;
    }
}
