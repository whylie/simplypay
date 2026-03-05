package com.welly.simplypay.service;

import com.welly.simplypay.model.Account;
import com.welly.simplypay.objects.AccountDTO;
import com.welly.simplypay.objects.CardDTO;
import com.welly.simplypay.repository.AccountRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class AccountService {
    private final AccountRepository accountRepository;


    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Page<AccountDTO> getAllAccounts(int page, int size, String sortBy) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy).ascending());
        return accountRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    public AccountDTO getAccountByAccountNumber(String accountNumber) {
        return  accountRepository.findByAccountNumber(accountNumber)
                .map(this::mapToResponse)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));
    }

    @Transactional
    public AccountDTO createAccount(AccountDTO request) {
        Account account = new Account();
        account.setAccountNumber(request.accountNumber());
        account.setAlias(request.alias());
        account.setCurrency(request.currency());
        account.setCurrentAmmount(BigDecimal.ZERO);

        Account saved = accountRepository.save(account);
        return mapToResponse(saved);
    }

    @Transactional
    public AccountDTO updateAccount(String accountNumber, AccountDTO request) {
        Account existingAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new EntityNotFoundException("Account not found"));

        existingAccount.setAlias(request.alias());
        existingAccount.setCurrentAmmount(request.balance());

        return mapToResponse(existingAccount);
    }

    private AccountDTO mapToResponse(Account account) {
        // Transform the list of Card entities into CardDTO records
        List<CardDTO> cardDtos = account.getCards().stream()
                .map(card -> new CardDTO(
                        card.getId(),
                        card.getCardNumber(),
                        card.getOwnerName(),
                        card.getCvv(),
                        card.getCardExpiryDate(),
                        account.getAccountNumber(),
                        card.getCardActive()
                )).toList();

        return new AccountDTO(
                account.getId(),
                account.getAccountNumber(),
                account.getAlias(),
                account.getCurrency(),
                account.getCurrentAmmount(),
                account.getCards()!= null ? cardDtos: new ArrayList<>()
        );
    }
}
