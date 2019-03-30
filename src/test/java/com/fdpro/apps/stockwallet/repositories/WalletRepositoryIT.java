package com.fdpro.apps.stockwallet.repositories;

import com.fdpro.apps.stockwallet.domain.CashFlow;
import com.fdpro.apps.stockwallet.domain.Symbol;
import com.fdpro.apps.stockwallet.domain.Transaction;
import com.fdpro.apps.stockwallet.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.fdpro.apps.stockwallet.WalletTestUtils.EURO;
import static com.fdpro.apps.stockwallet.WalletTestUtils.EURO_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class WalletRepositoryIT {
    private Wallet wallet;
    private Symbol symbol;

    @Autowired
    private WalletRepository repository;

    @BeforeEach
    private void beforeEach() {
        wallet = new Wallet("My wallet");
        symbol = new Symbol("ABI", "AB Inbev");
    }

    @Test
    void save_NoOperations() {
        repository.saveAndFlush(wallet);

        Wallet foundWallet = getIfFoundFailOtherwise();
        verifyContent(foundWallet);
    }

    @Test
    void save_CashFlows() {
        wallet.register(CashFlow.deposit(EURO_AMOUNT));
        wallet.register(CashFlow.withdrawal(EURO_AMOUNT));
        repository.saveAndFlush(wallet);

        Wallet foundWallet = getIfFoundFailOtherwise();
        verifyContent(foundWallet);
    }

    @Test
    void save_Transactions() {
        wallet.register(Transaction.buy(2, symbol).atPrice(EURO_AMOUNT).build());
        wallet.register(Transaction.sell(1, symbol).atPrice(EURO_AMOUNT).build());
        repository.saveAndFlush(wallet);

        Wallet foundWallet = getIfFoundFailOtherwise();
        verifyContent(foundWallet);
    }

    @Test
    void save_CashFlowsAndTransactions() {
        wallet.register(CashFlow.deposit(EURO_AMOUNT));
        wallet.register(Transaction.buy(1, symbol).atPrice(EURO_AMOUNT).build());
        repository.saveAndFlush(wallet);

        Wallet foundWallet = getIfFoundFailOtherwise();
        verifyContent(foundWallet);
    }

    private Wallet getIfFoundFailOtherwise() {
        Optional<Wallet> maybeFoundWallet = repository.findById(wallet.getName());
        assertTrue(maybeFoundWallet.isPresent());
        return maybeFoundWallet.get();
    }

    private void verifyContent(Wallet foundWallet) {
        assertEquals(wallet, foundWallet);
        assertEquals(wallet.getName(), foundWallet.getName());
        assertEquals(wallet.amount(EURO), foundWallet.amount(EURO));
        assertEquals(wallet.count(symbol), foundWallet.count(symbol));
    }
}