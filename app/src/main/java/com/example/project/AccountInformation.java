package com.example.project;

public class AccountInformation {

    private String type;
    private String money;
    private String owner;
    private String name;
    private String number;

    public AccountInformation(){

    }

    public AccountInformation(String type, String money, String owner, String name, String number) {
        this.type = type;
        this.money = money;
        this.owner = owner;
        this.name = name;
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getMoney(){
        return money;
    }

    public void setMoney(String money){
        this.money = money;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
