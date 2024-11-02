package com.example.demo.response;

import java.time.LocalDateTime;

public class TransactionHistoryResponse {
    private String invoiceNumber;
    private String transactionType;
    private String description;
    private double totalAmount;
    private LocalDateTime createdOn;

    public TransactionHistoryResponse(String invoiceNumber, String transactionType, String description, double totalAmount, LocalDateTime createdOn) {
        this.invoiceNumber = invoiceNumber;
        this.transactionType = transactionType;
        this.description = description;
        this.totalAmount = totalAmount;
        this.createdOn = createdOn;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
