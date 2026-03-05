package com.welly.simplypay.utilties;

public class PiiMaskingUtilities {
    // Masks card number: 4366512771678160 -> 436651******8160
    public static String maskCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 10) return "####-####-####";
        return cardNumber.replaceAll("(\\d{6})\\d+(\\d{4})", "$1******$2");
    }

    // Completely hides CVV
    public static String maskCVV(Object cvv) {
        return "***";
    }
}
