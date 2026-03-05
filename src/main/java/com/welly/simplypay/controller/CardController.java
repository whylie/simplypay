package com.welly.simplypay.controller;


import com.welly.simplypay.objects.CardDTO;
import com.welly.simplypay.service.CardService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/cards")
public class CardController {

    private final CardService cardService;

    public CardController(CardService cardService) {
        this.cardService = cardService;
    }
    @GetMapping
    public ResponseEntity<Page<CardDTO>> getAllCards(@RequestParam(defaultValue = "0") int page,
                                                        @RequestParam(defaultValue = "10") int size,
                                                        @RequestParam(defaultValue = "cardNumber") String sortBy) {
        Page<CardDTO> cardResult =  cardService.getAllCards(page,size,sortBy);
        return ResponseEntity.ok(cardResult);
    }

    /**
     * POST /api/v1/cards
     * Body: { "accountNumber": "880000000001", "cardNumber": "4111222233334444","cvv":"813", "cardExpiryDate" : "03/28" }
     */
    @PostMapping
    public ResponseEntity<CardDTO> createNewCard(@RequestBody Map<String, String> request) {
        CardDTO response = cardService.createNewCard(
                request.get("accountNumber")
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * PATCH /api/v1/cards/{cardNumber}/status
     * Body: { "active": true }
     */
    @PatchMapping("/{cardNumber}/status")
    public ResponseEntity<CardDTO> updateCardStatus(
            @PathVariable String cardNumber,
            @RequestBody Map<String, Boolean> statusRequest) {
        return ResponseEntity.ok(cardService.toggleCardStatus(cardNumber, statusRequest.get("active")));
    }

    /**
     * PATCH /api/v1/cards/{cardNumber}/
     * Body: { "active": true }
     */
    @PatchMapping("/{cardNumber}")
    public ResponseEntity<CardDTO> updateCard(
            @PathVariable String cardNumber,
            @RequestBody CardDTO request) {
        return ResponseEntity.ok(cardService.updateCard(cardNumber, request));
    }



}
