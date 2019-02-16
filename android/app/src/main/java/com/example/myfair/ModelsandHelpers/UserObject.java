package com.example.myfair.ModelsandHelpers;

public class UserObject {
    private String fullName;
    private int age;

    public UserObject(String fN, int a){
        this.fullName = fN;
        this.age = a;
    }

    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }
}
