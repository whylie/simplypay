package com.welly.simplypay.service;

import com.welly.simplypay.client.CardGeneratorClient;
import com.welly.simplypay.exception.ApplicationException;
import com.welly.simplypay.model.Account;
import com.welly.simplypay.model.Card;
import com.welly.simplypay.objects.CardDTO;
import com.welly.simplypay.objects.externalCards.ExternalCardInfo;
import com.welly.simplypay.objects.externalCards.ExternalCardResponse;
import com.welly.simplypay.repository.AccountRepository;
import com.welly.simplypay.repository.CardRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private CardGeneratorClient cardGeneratorClient;

    private CardService cardService;

    @BeforeEach
    void setUp() {
        this.cardService = new CardService(cardRepository, accountRepository, cardGeneratorClient);
    }

    @Test
    void shouldGenerateCardAndLinkToAccount() {
        // Arrange
        String accNum = "880088008800";
        Account mockAccount = new Account();
        mockAccount.setAccountNumber(accNum);

        ExternalCardInfo mockExternalInfo = new ExternalCardInfo("4111222233334444", "12/2030", "Test Bank", 123);

        Mockito.when(accountRepository.findByAccountNumber(accNum)).thenReturn(Optional.of(mockAccount));
        Mockito.when(cardGeneratorClient.generateNewCard()).thenReturn(mockExternalInfo);
        Mockito.when(cardRepository.save(Mockito.any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CardDTO result = cardService.createNewCard(accNum);

        // Assert
        Assertions.assertNotNull(result);
        Assertions.assertEquals("4111222233334444", result.cardNumber());

    }

    @Test
    void shouldThrowExceptionWhenAccountNotFound() {
        Mockito.when(accountRepository.findByAccountNumber("000")).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> cardService.createNewCard("000"));
    }
}
