package com.fdpro.apps.stockwallet.domain;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static com.fdpro.apps.stockwallet.WalletTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WalletTest {
    private Wallet wallet;

    @BeforeEach
    private void beforeEach() {
        wallet = new Wallet("My wallet");
    }

    @Test
    void newWallet_NullName() {
        assertThrows(IllegalArgumentException.class, () -> new Wallet(null));
    }

    @Test
    void newWallet_EmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Wallet(""));
    }

    @Test
    void newWallet_BlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Wallet("   "));
    }

    @Test
    void register_CashFlow_Null() {
        assertThrows(IllegalArgumentException.class, () -> wallet.register((CashFlow) null));
    }

    @Test
    void amount_Null() {
        assertThrows(IllegalArgumentException.class, () -> wallet.amount(null));
    }

    @Test
    void amount_NoOperations() {
        assertEquals(ZERO_EUROS, wallet.amount(EURO));
    }

    @Test
    void amount_NoCurrencyOperations() {
        wallet.register(Transaction.buy(1, SYMBOL).atPrice(EURO_AMOUNT).build());

        assertEquals(ZERO_DOLLARS, wallet.amount(DOLLARS));
    }

    @Test
    void amount_OtherSymbolOperations() {
        wallet.register(CashFlow.deposit(EURO_AMOUNT));

        assertEquals(ZERO_DOLLARS, wallet.amount(DOLLARS));
    }

    @Test
    void amount_OneOperation() {
        wallet.register(CashFlow.deposit(EURO_AMOUNT));

        assertEquals(EURO_AMOUNT, wallet.amount(EURO));
    }

    @Test
    void amount_MultipleOperations() {
        wallet.register(CashFlow.deposit(EURO_AMOUNT));
        wallet.register(CashFlow.deposit(EURO_AMOUNT));

        MonetaryAmount expectedAmount = Money.of(BigDecimal.valueOf(3.0), EURO);
        assertEquals(expectedAmount, wallet.amount(EURO));
    }

    @Test
    void count_Null() {
        assertThrows(IllegalArgumentException.class, () -> wallet.count(null));
    }

    @Test
    void count_NoOperations() {
        assertEquals(0, wallet.count(SYMBOL));
    }

    @Test
    void count_NoSymbolOperations() {
        wallet.register(CashFlow.deposit(EURO_AMOUNT));

        assertEquals(0, wallet.count(SYMBOL));
    }

    @Test
    void count_OtherSymbolOperations() {
        wallet.register(Transaction.buy(1, SYMBOL).atPrice(EURO_AMOUNT).build());

        assertEquals(0, wallet.count(new Symbol("ABI", "AB Inbev")));
    }

    @Test
    void count_OneOperation() {
        wallet.register(Transaction.buy(1, SYMBOL).atPrice(EURO_AMOUNT).build());

        assertEquals(1, wallet.count(SYMBOL));
    }

    @Test
    void count_MultipleOperations() {
        wallet.register(Transaction.buy(3, SYMBOL).atPrice(EURO_AMOUNT).forCost(EURO_AMOUNT).build());
        wallet.register(Transaction.sell(1, SYMBOL).atPrice(EURO_AMOUNT).forCost(EURO_AMOUNT).build());

        assertEquals(2, wallet.count(SYMBOL));
    }
}