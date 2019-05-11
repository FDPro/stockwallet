package com.fdpro.apps.stockwallet.wallet.repositories;

import com.fdpro.apps.stockwallet.symbol.repositories.SymbolRepository;
import com.fdpro.apps.stockwallet.wallet.domain.CashFlow;
import com.fdpro.apps.stockwallet.symbol.domain.Symbol;
import com.fdpro.apps.stockwallet.wallet.domain.Transaction;
import com.fdpro.apps.stockwallet.wallet.domain.Wallet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static com.fdpro.apps.stockwallet.wallet.WalletTestUtils.EURO;
import static com.fdpro.apps.stockwallet.wallet.WalletTestUtils.EURO_AMOUNT;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
class WalletRepositoryIT {
    private Wallet wallet;
    private Symbol symbol;

    @Autowired
    private SymbolRepository symbolRepository;
    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    private void beforeEach() {
        symbol = new Symbol("ABI", "AB Inbev");
        symbolRepository.save(symbol);

        wallet = new Wallet("My wallet");
    }

    @Test
    void save_NoOperations() {
        walletRepository.saveAndFlush(wallet);

        Wallet foundWallet = getIfFoundFailOtherwise();
        verifyContent(foundWallet);
    }

    @Test
    void save_CashFlows() {
        wallet.register(CashFlow.deposit(EURO_AMOUNT));
        wallet.register(CashFlow.withdrawal(EURO_AMOUNT));
        walletRepository.saveAndFlush(wallet);

        Wallet foundWallet = getIfFoundFailOtherwise();
        verifyContent(foundWallet);
    }

    @Test
    void save_Transactions() {
        wallet.register(Transaction.buy(2, symbol).atPrice(EURO_AMOUNT).build());
        wallet.register(Transaction.sell(1, symbol).atPrice(EURO_AMOUNT).build());
        walletRepository.saveAndFlush(wallet);

        Wallet foundWallet = getIfFoundFailOtherwise();
        verifyContent(foundWallet);
    }

    @Test
    void save_CashFlowsAndTransactions() {
        wallet.register(CashFlow.deposit(EURO_AMOUNT));
        wallet.register(Transaction.buy(1, symbol).atPrice(EURO_AMOUNT).build());
        walletRepository.saveAndFlush(wallet);

        Wallet foundWallet = getIfFoundFailOtherwise();
        verifyContent(foundWallet);
    }

    private Wallet getIfFoundFailOtherwise() {
        Optional<Wallet> maybeFoundWallet = walletRepository.findById(wallet.getName());
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