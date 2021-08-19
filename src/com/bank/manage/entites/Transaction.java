package com.bank.manage.entites;

import java.time.LocalDateTime;

import com.bank.manage.Enums.TransactionType;

public class Transaction {
	
    private String id;
    private double amount;
    private String sentByAccNO;
    private String sentByName;
    private String receiverAccNO;
    private String receiverName;
    private TransactionType transactionType;
    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    private LocalDateTime date;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getSentByAccNO() {
        return sentByAccNO;
    }

    public void setSentByAccNO(String sentByAccNO) {
        this.sentByAccNO = sentByAccNO;
    }

    public String getSentByName() {
        return sentByName;
    }

    public void setSentByName(String sentByName) {
        this.sentByName = sentByName;
    }

    public String getReceiverAccNO() {
        return receiverAccNO;
    }

    public void setReceiverAccNO(String receiverAccNO) {
        this.receiverAccNO = receiverAccNO;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }


}