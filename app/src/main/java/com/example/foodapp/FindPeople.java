package com.example.foodapp;

public class FindPeople {

    private String Nickname, fullName, image;

    public FindPeople(){}

    public FindPeople(String nickname, String fullName, String image) {
        Nickname = nickname;
        this.fullName = fullName;
        this.image = image;
    }



    public String getNickname() {
        return Nickname;
    }

    public void setNickname(String nickname) {
        Nickname = nickname;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
