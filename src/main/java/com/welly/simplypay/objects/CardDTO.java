package com.welly.simplypay.objects;

import com.welly.simplypay.utilties.PiiMaskingUtilities;

import java.time.YearMonth;

public record CardDTO(
        Long id,
        String cardNumber,
        String ownerName,
        Integer cvv,
        YearMonth cardExpiryDate,
        String accountNumber,
        Boolean isCardActive)
{
    @Override
    public String toString() {
        return "CardDTO{" +
                "  cardNumber='" + PiiMaskingUtilities.maskCardNumber(cardNumber) + '\'' +
                ", ownerName='" + ownerName + '\'' +
                ", cvv=" + PiiMaskingUtilities.maskCVV(cvv) +
                ", cardExpiryDate=" + cardExpiryDate +
                ", accountNumber='" + accountNumber + '\'' +
                ", isCardActive=" + isCardActive +
                '}';
    }
}
