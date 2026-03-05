package com.welly.simplypay.objects.externalCards;

import com.welly.simplypay.utilties.PiiMaskingUtilities;

public record ExternalCardInfo(
        String number,
        String expiration,
        String issuer,
        int cvv
) {
    @Override
    public String toString() {
        return "ExternalCardInfo{" +
                "number='" + PiiMaskingUtilities.maskCardNumber(number) + '\'' +
                ", expiration='" + expiration + '\'' +
                ", issuer='" + issuer + '\'' +
                ", cvv=" + PiiMaskingUtilities.maskCVV(cvv) +
                '}';
    }
}
