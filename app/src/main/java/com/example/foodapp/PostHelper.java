package com.example.foodapp;

public class PostHelper {
    private String Instructions, Title, overallRating, userCreatorID, userName, date, time, Ingredients;

    public PostHelper(){}

    public PostHelper(String instructions, String title, String overallRating, String userCreatorID, String userName, String date, String time, String ingredients) {
        Instructions = instructions;
        Title = title;
        this.overallRating = overallRating;
        this.userCreatorID = userCreatorID;
        this.userName = userName;
        this.date = date;
        this.time = time;
        Ingredients = ingredients;
    }

    public String getInstructions() {
        return Instructions;
    }

    public void setInstructions(String instructions) {
        Instructions = instructions;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public String getUserCreatorID() {
        return userCreatorID;
    }

    public void setUserCreatorID(String userCreatorID) {
        this.userCreatorID = userCreatorID;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getIngredients() {
        return Ingredients;
    }

    public void setIngredients(String ingredients) {
        Ingredients = ingredients;
    }
}

