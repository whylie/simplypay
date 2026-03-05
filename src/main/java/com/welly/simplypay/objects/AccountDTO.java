package com.welly.simplypay.objects;

import jakarta.persistence.Column;

import java.math.BigDecimal;
import java.util.List;

public record AccountDTO (
        Long id,
        String accountNumber,
        String alias,
        String currency,
        BigDecimal balance,
        List<CardDTO> cards
)
{}
