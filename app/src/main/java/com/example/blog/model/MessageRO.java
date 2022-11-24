package com.example.blog.model;

public class MessageRO{
    protected int id;
    protected String content;
    protected String type;
    protected MemberRO sender;
    protected MemberRO receiver;
    protected String created_at;
    protected Image[] attachments;

    public Image[] getAttachments() {
        return attachments;
    }

    public void setAttachments(Image[] attachments) {
        this.attachments = attachments;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public MemberRO getSender() {
        return sender;
    }

    public void setSender(MemberRO sender) {
        this.sender = sender;
    }

    public MemberRO getReceiver() {
        return receiver;
    }

    public void setReceiver(MemberRO receiver) {
        this.receiver = receiver;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }
}
