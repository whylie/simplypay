package com.welly.simplypay.objects.externalCards;

import java.util.List;

public record ExternalCardData(
        String brand,
        List<ExternalCardInfo> cards)
{ }
