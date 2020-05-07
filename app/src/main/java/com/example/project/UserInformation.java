package com.example.project;

public class UserInformation {

    private String firstName;
    private String lastName;
    private String address;
    private String userID;

    public UserInformation(){

    }

    public UserInformation(String firstName,String lastName,String address,String userID){
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.userID = userID;
    }

    public String getFirstName(){
        return  firstName;
    }

    public void setFirstName(String firstName){
        this.firstName = firstName;
    }

    public String getLastName(){
        return lastName;
    }

    public void setLastName(String lastName){
        this.lastName = lastName;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address){
        this.address = address;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
