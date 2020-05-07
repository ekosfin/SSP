package com.example.project;

public class CardInformation {

    private String linkedAccount;
    private String AccountID;
    private String type;
    private String usageLimit;
    private String nimi;

    public CardInformation(){

    }

    public CardInformation(String linkedAccount,String type,String usageLimit,String nimi,String accountID){
        this.linkedAccount = linkedAccount;
        this.type = type;
        this.usageLimit = usageLimit;
        this.nimi = nimi;
        this.AccountID = accountID;
    }

    public String getLinkedAccount(){
        return linkedAccount;
    }

    public void setLinkedAccount(String linkedAccount){
        this.linkedAccount = linkedAccount;
    }

    public String getType(){
        return type;
    }

    public void setType(String type){
        this.type = type;
    }

    public String getUsageLimit() {
        return usageLimit;
    }

    public void setUsageLimit(String usageLimit) {
        this.usageLimit = usageLimit;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getAccountID() {
        return AccountID;
    }

    public void setAccountID(String accountID) {
        AccountID = accountID;
    }
}
