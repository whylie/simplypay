package com.welly.simplypay.client;

import com.welly.simplypay.exception.ApplicationException;
import com.welly.simplypay.filter.ApiLoggingFilter;
import com.welly.simplypay.objects.externalCards.ExternalCardInfo;
import com.welly.simplypay.objects.externalCards.ExternalCardResponse;
import com.welly.simplypay.utilties.PiiMaskingUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CardGeneratorClient {
    private final RestClient restClient;
    private static final Logger logger = LoggerFactory.getLogger(ApiLoggingFilter.class);


    public CardGeneratorClient(RestClient restClient) {
        this.restClient = restClient;
    }

    /**
     * Calls the external API to generate a new credit card.
     * The API Key is automatically handled by the RestClient bean configuration.
     */
    public ExternalCardInfo generateNewCard() {
        logger.info("Requesting new card generation from external provider");

        try {
            ExternalCardResponse response = restClient.get()
                    .uri(uriBuilder ->  uriBuilder
                            .path("/v1/cardgenerator")
                            .queryParam("brand","visa")
                            .build())
                    .retrieve()
                    .onStatus(status -> status.is4xxClientError() || status.is5xxServerError(), (request, res) -> {
                        logger.error("External API error: Status Code {}", res.getStatusCode());
                        throw new ApplicationException("External Card Provider Unavailable", "EXTERNAL_API_ERROR", HttpStatus.BAD_GATEWAY);
                    })
                    .body(ExternalCardResponse.class);

            if (response != null && response.data() != null && !response.data().cards().isEmpty()) {
                ExternalCardInfo info = response.data().cards().get(0);

                // Masking the number in logs for security
                logger.info("Successfully received card from provider: {}", PiiMaskingUtilities.maskCardNumber(info.number()));
                return info;
            }

            throw new ApplicationException("Empty response from card provider", "EXTERNAL_API_ERROR", HttpStatus.BAD_REQUEST);

        } catch (Exception e) {
            logger.error("Failed to connect to External Card API: {}", e.getMessage());
            throw new ApplicationException("Connection to Card Provider failed", "EXTERNAL_API_ERROR", HttpStatus.BAD_GATEWAY);
        }
    }
}
