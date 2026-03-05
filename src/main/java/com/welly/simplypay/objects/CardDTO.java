package com.welly.simplypay.objects;

import com.welly.simplypay.utilties.PiiMaskingUtilities;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.YearMonth;

public record CardDTO(
        Long id,
        @NotBlank(message = "Card Number Cannot Be Empty")
        String cardNumber,
        String ownerName,
        @NotBlank(message = "CVV Cannot Be Empty")
        @Size(min = 3, max = 3, message = "CVV must be exactly 3 digits")
        Integer cvv,
        YearMonth cardExpiryDate,
        @NotBlank(message = "Account Number Cannot Be Empty")
        String accountNumber,
        Boolean isCardActive)
{
    @Override
    public String toString() {
        return "CardDTO{" +
                "  cardNumber='" + cardNumber + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", cvv=" + cvv +
                ", cardExpiryDate=" + cardExpiryDate +
                ", accountNumber='" + accountNumber + '\'' +
                ", isCardActive=" + isCardActive +
                '}';
    }
}
