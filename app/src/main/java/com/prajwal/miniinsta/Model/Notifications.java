package com.prajwal.miniinsta.Model;

public class Notifications {
    private String userId, text, postid;
    private boolean isPost;

    public Notifications() {
    }

    public Notifications(String userId, String text, String postid, boolean isPost) {
        this.userId = userId;
        this.text = text;
        this.postid = postid;
        this.isPost = isPost;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public boolean isPost() {
        return isPost;
    }

    public void setPost(boolean post) {
        isPost = post;
    }
}
