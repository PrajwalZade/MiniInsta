package com.prajwal.miniinsta.Model;

public class Post {
    private String description;
    private String imgUrl;
    private String postID;
    private String publisher;

    public Post() {
    }

    public Post(String description, String imgUrl, String postID, String publisher) {
        this.description = description;
        this.imgUrl = imgUrl;
        this.postID = postID;
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
