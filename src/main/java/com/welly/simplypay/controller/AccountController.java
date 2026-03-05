package com.welly.simplypay.controller;

import com.welly.simplypay.model.Account;
import com.welly.simplypay.model.Card;
import com.welly.simplypay.objects.AccountDTO;
import com.welly.simplypay.objects.CardDTO;
import com.welly.simplypay.service.AccountService;
import com.welly.simplypay.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final CardService cardService;

    public AccountController(AccountService accountService, CardService cardService) {
        this.accountService = accountService;
        this.cardService = cardService;
    }

    @GetMapping
    public ResponseEntity<Page<AccountDTO>> getAllAccounts(@RequestParam(defaultValue = "0") int page,
                                                     @RequestParam(defaultValue = "10") int size,
                                                     @RequestParam(defaultValue = "accountNumber") String sortBy) {
        Page<AccountDTO> accountPage =  accountService.getAllAccounts(page,size,sortBy);
        return ResponseEntity.ok(accountPage);
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> getAccountByAccountNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByAccountNumber(accountNumber));
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(accountService.createAccount(request));
    }

    @PatchMapping("/{accountNumber}")
    public ResponseEntity<AccountDTO> updateAccount(
            @PathVariable String accountNumber,
            @RequestBody AccountDTO request) {
        return ResponseEntity.ok(accountService.updateAccount(accountNumber, request));
    }

    @GetMapping("/{accountNumber}/cards")
    public ResponseEntity<Page<CardDTO>> getAccountCards(
            @PathVariable String accountNumber,
            @PageableDefault(size = 10) Pageable pageable) {

        return ResponseEntity.ok(cardService.getCardsByAccountNumber(accountNumber, pageable));
    }
}
