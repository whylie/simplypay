package com.welly.simplypay.service;

import com.welly.simplypay.client.CardGeneratorClient;
import com.welly.simplypay.model.Account;
import com.welly.simplypay.model.Card;
import com.welly.simplypay.objects.CardDTO;
import com.welly.simplypay.objects.externalCards.ExternalCardInfo;
import com.welly.simplypay.repository.AccountRepository;
import com.welly.simplypay.repository.CardRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

@Service
public class CardService {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;
    private final CardGeneratorClient cardGeneratorClient;

    public CardService(CardRepository cardRepository, AccountRepository accountRepository, CardGeneratorClient cardGeneratorClient) {
        this.cardRepository = cardRepository;
        this.accountRepository = accountRepository;
        this.cardGeneratorClient = cardGeneratorClient;
    }

    @Transactional(readOnly = true)
    public Page<CardDTO> getAllCards(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return cardRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Transactional(readOnly = true)
    public Page<CardDTO> getCardsByAccountNumber(String accountNumber, Pageable pageable) {
        if (!accountRepository.existsByAccountNumber(accountNumber)) {
            throw new EntityNotFoundException("Account not found: " + accountNumber);
        }

        return cardRepository.findByAccountAccountNumber(accountNumber, pageable)
                .map(this::mapToResponse);
    }

    @Transactional
    public CardDTO createNewCard(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        // Call External API to get card number, cvv and expiry date
        ExternalCardInfo cardInfo = cardGeneratorClient.generateNewCard();


        Card card = new Card();
        card.setCardNumber(cardInfo.number());
        card.setCvv(cardInfo.cvv());
        card.setCardExpiryDate(transformCardExpiryDate(cardInfo.expiration()));
        card.setAccount(account);
        card.setCardActive(false);

        return mapToResponse(cardRepository.save(card));
    }

    @Transactional
    public CardDTO updateCard(String cardNumber, CardDTO request) {
        Card existingCard = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        existingCard.setOwnerName(request.ownerName());

        return mapToResponse(existingCard);
    }

    @Transactional
    public CardDTO toggleCardStatus(String cardNumber, boolean active) {
        Card card = cardRepository.findByCardNumber(cardNumber)
                .orElseThrow(() -> new EntityNotFoundException("Card not found"));

        card.setCardActive(active);
        return mapToResponse(card);
    }

    public YearMonth transformCardExpiryDate(String expiration) {
        // Define the pattern that matches the input string format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/yyyy");
        YearMonth cardExpiryDAte = YearMonth.parse(expiration, formatter);
        return cardExpiryDAte;
    }

    private CardDTO mapToResponse(Card card) {
        return new CardDTO(
                card.getId(),
                card.getCardNumber(),
                card.getOwnerName(),
                card.getCvv(),
                card.getCardExpiryDate(),
                card.getAccount().getAccountNumber(),
                card.getCardActive()
        );
    }
}
