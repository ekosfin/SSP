package com.example.project;

import java.util.Date;

public class TransactionInformation {

    private String sender;
    private String receiver;
    private String amount;
    private String message;
    private Date transactionDate;

    public TransactionInformation(){

    }

    public TransactionInformation(String sender, String reciver, String amount, String message, Date transactionDate){
        this.sender = sender;
        this.receiver = reciver;
        this.amount = amount;
        this.message = message;
        this.transactionDate = transactionDate;
    }

    public String getSender(){
        return sender;
    }

    public void setSender(String sender){
        this.sender = sender;
    }

    public String getReceiver(){
        return receiver;
    }

    public void setReceiver(String receiver){
        this.receiver = receiver;
    }

    public String getAmount(){
        return amount;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getMessage(){
        return message;
    }

    public void setMessage(String message){
        this.message = message;
    }

    public Date getDate(){
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate){
        this.transactionDate = transactionDate;
    }
}
