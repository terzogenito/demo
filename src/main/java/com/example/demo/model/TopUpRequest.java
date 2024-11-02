package com.example.demo.model;

import jakarta.validation.constraints.NotNull;

public class TopUpRequest {

    @NotNull(message = "Top-up amount is required.")
    private Double topUpAmount;

    public TopUpRequest() {}

    public TopUpRequest(Double topUpAmount) {
        this.topUpAmount = topUpAmount;
    }

    public Double getTopUpAmount() {
        return topUpAmount;
    }

    public void setTopUpAmount(Double topUpAmount) {
        this.topUpAmount = topUpAmount;
    }
}
