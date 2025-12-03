package com.bankingSystem.dto;

import jakarta.validation.constraints.NotNull;

public class AmountRequest {

    @NotNull(message = "Amount is required")
    private Double amount;

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }
}
