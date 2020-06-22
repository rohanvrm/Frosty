package com.example.frosty.Models;

public class PostModel {
    private String Profile,username,time,caption,photo,likecount,commentcount,sharecount;

    public PostModel(String profile, String username, String time, String caption, String photo, String likecount, String commentcount, String sharecount) {
        Profile = profile;
        this.username = username;
        this.time = time;
        this.caption = caption;
        this.photo = photo;
        this.likecount = likecount;
        this.commentcount = commentcount;
        this.sharecount = sharecount;
    }

    public String getProfile() {
        return Profile;
    }

    public void setProfile(String profile) {
        Profile = profile;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getLikecount() {
        return likecount;
    }

    public void setLikecount(String likecount) {
        this.likecount = likecount;
    }

    public String getCommentcount() {
        return commentcount;
    }

    public void setCommentcount(String commentcount) {
        this.commentcount = commentcount;
    }

    public String getSharecount() {
        return sharecount;
    }

    public void setSharecount(String sharecount) {
        this.sharecount = sharecount;
    }
}
