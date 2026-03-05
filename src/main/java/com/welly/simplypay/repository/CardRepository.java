package com.welly.simplypay.repository;

import com.welly.simplypay.model.Card;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findByAccountAccountNumberAndIsCardActiveTrue(String accountNumber);
    Page<Card> findByAccountAccountNumber(String accountNumber, Pageable pageable);
    Optional<Card> findByCardNumber(String cardNumber);
}
