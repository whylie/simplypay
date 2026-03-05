package com.welly.simplypay.utilties;

import com.welly.simplypay.model.Account;
import com.welly.simplypay.model.Card;
import com.welly.simplypay.repository.AccountRepository;
import com.welly.simplypay.repository.CardRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    public DataInitializer(AccountRepository accountRepository, CardRepository cardRepository) {
        this.accountRepository = accountRepository;
        this.cardRepository = cardRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            // Define the minimum and maximum values (inclusive)
            int min = 100;
            int max = 999;

            // Create an instance of the Random class
            Random random = new Random();

            // Generate the random number within the range
            // The formula for a range [min, max] is: random.nextInt(max - min + 1) + min
            List<Account> testAccounts = new ArrayList<>();

            if (accountRepository.count() == 0) {
                for (int i = 1; i <= 10; i++) {
                    Account acc = new Account();
                    acc.setAccountNumber(String.format("88%010d", i));
                    acc.setAlias("User Account " + i);
                    acc.setCurrency("MYR");
                    acc.setCurrentAmmount(BigDecimal.ZERO);
                    Account savedAcc = accountRepository.save(acc);

                    // Card 1: Active
                    Card activeCard = new Card();
                    activeCard.setCardNumber("411100000000" + String.format("%04d", i));
                    activeCard.setOwnerName("Card User Active "+i);
                    activeCard.setCardActive(true);
                    activeCard.setCvv(random.nextInt(max - min + 1) + min);
                    activeCard.setAccount(savedAcc);
                    cardRepository.save(activeCard);

                    // Card 2: Inactive (Expired/Old)
                    Card oldCard = new Card();
                    oldCard.setCardNumber("522200000000" + String.format("%04d", i));
                    oldCard.setCvv(random.nextInt(max - min + 1) + min);
                    oldCard.setOwnerName("Card User Deactive "+i);
                    oldCard.setCardActive(false);
                    oldCard.setAccount(savedAcc);
                    cardRepository.save(oldCard);
                }
            }

            accountRepository.saveAll(testAccounts);
            System.out.println(">> Generated 20 MYR Test Accounts successfully.");
        }
    }
}
