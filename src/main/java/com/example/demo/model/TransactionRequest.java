package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;

public class TransactionRequest {

    @NotBlank(message = "Service code is required.")
    private String serviceCode;

    public TransactionRequest() {}

    public TransactionRequest(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }
}
