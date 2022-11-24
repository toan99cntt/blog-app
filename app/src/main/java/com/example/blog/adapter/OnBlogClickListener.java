package com.example.blog.adapter;

import com.example.blog.model.BlogRO;

public interface OnBlogClickListener {
    void onBlogClickListener(BlogRO b);
    void onBlogClickCommentListener(BlogRO b);
    void onBlogClickLikeListener(BlogRO b);
    void onBlogClickSettingListener(BlogRO b);
}
