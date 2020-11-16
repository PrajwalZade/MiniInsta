package com.prajwal.miniinsta.Model;

public class Comment {
    private String ID;
    private String Comments;
    private String publisher;
    public Comment() {
    }

    public Comment(String ID, String comments, String publisher) {
        this.ID = ID;
        Comments = comments;
        this.publisher = publisher;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
