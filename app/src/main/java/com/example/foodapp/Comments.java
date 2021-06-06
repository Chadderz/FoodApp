package com.example.foodapp;

public class Comments {

    private String commentText, date, time, userName;

    public Comments(){}

    public Comments(String commentText, String date, String time, String userName) {
        this.commentText = commentText;
        this.date = date;
        this.time = time;
        this.userName = userName;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        this.commentText = commentText;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
