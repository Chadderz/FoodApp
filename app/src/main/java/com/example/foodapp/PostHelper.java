package com.example.foodapp;

public class PostHelper {
    String receipeName, overallRating, receipeInstructions, creatorID;


    public PostHelper(){}

    public PostHelper(String receipeName, String overallRating, String receipeInstructions, String creatorID){
        this.receipeName = receipeName;
        this.overallRating = overallRating;
        this.receipeInstructions = receipeInstructions;
        this.creatorID = creatorID;
    }

    public String getReceipeName() {
        return receipeName;
    }

    public void setReceipeName(String receipeName) {
        this.receipeName = receipeName;
    }

    public String getOverallRating() {
        return overallRating;
    }

    public void setOverallRating(String overallRating) {
        this.overallRating = overallRating;
    }

    public String getReceipeInstructions() {
        return receipeInstructions;
    }

    public void setReceipeInstructions(String receipeInstructions) {
        this.receipeInstructions = receipeInstructions;
    }
}
