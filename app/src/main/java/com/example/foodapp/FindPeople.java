package com.example.foodapp;

public class FindPeople {

    private String Nickname, fullName;

    public FindPeople(){}

    public FindPeople(String nickname, String fullName) {
        Nickname = nickname;
        this.fullName = fullName;
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
}
