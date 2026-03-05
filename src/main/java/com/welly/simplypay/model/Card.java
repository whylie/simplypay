package com.welly.simplypay.model;

import jakarta.persistence.*;

import java.time.YearMonth;
import java.time.ZoneId;

@Entity
@Table(name = "cards", uniqueConstraints = {
        @UniqueConstraint(columnNames = "cardNumber")
})
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String cardNumber;
    private String ownerName;
    private Integer cvv;
    private YearMonth cardExpiryDate;
    private Boolean isCardActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Integer getCvv() {
        return cvv;
    }

    public void setCvv(Integer cvv) {
        this.cvv = cvv;
    }

    public YearMonth getCardExpiryDate() {
        return cardExpiryDate;
    }

    public void setCardExpiryDate(YearMonth cardExpiryDate) {
        this.cardExpiryDate = cardExpiryDate;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }


    public Boolean getCardActive() {
        return isCardActive;
    }

    public void setCardActive(Boolean cardActive) {
        isCardActive = cardActive;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    /**
     * Checks if the card is expired as of the current month.
     * The card is valid through the entire expiration month.
     */
    public boolean isCardExpired() {
        YearMonth currentMonth = YearMonth.now(ZoneId.systemDefault());
        // A card is not expired if the current month is the same as or before the expiry month.
        return currentMonth.isAfter(cardExpiryDate);
    }
}
