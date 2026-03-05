package com.welly.simplypay.objects.externalCards;

public record ExternalCardResponse(
        String status,
        ExternalCardData data
) {
}
