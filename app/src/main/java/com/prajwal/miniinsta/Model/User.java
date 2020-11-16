package com.prajwal.miniinsta.Model;

public class User {
    private String Email;
    private String UserName;
    private String Name;
    private String imageurl;
    private String ID;
    private String Bio;

    public User() {
    }

    public User(String email, String userName, String name, String imageurl, String ID, String bio) {
        Email = email;
        UserName = userName;
        Name = name;
        this.imageurl = imageurl;
        this.ID = ID;
        Bio = bio;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBio() {
        return Bio;
    }

    public void setBio(String bio) {
        Bio = bio;
    }
}
