package com.welly.simplypay.objects;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.List;

public record AccountDTO (
        Long id,
        @NotBlank(message = "Account number is required")
        @Size(min = 12, max = 12, message = "Account number must be exactly 12 digits")
        String accountNumber,
        String alias,
        @NotBlank(message = "Account must have currency")
        String currency,
        @NotNull(message = "Initial balance is required")
        @PositiveOrZero(message = "Balance cannot be negative")
        BigDecimal balance,
        List<CardDTO> cards
)
{}
