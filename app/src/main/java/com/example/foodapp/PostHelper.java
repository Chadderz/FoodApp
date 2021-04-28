package com.example.foodapp;

public class PostHelper {
    private String Instructions, Title, overallRating, userCreatorID;

    public PostHelper(){}

    public PostHelper(String instructions, String title, String overallRating, String userCreatorID) {
        Instructions = instructions;
        Title = title;
        this.overallRating = overallRating;
        this.userCreatorID = userCreatorID;
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
}

