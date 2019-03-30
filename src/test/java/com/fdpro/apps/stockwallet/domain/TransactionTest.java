package com.fdpro.apps.stockwallet.domain;

import org.javamoney.moneta.Money;
import org.junit.jupiter.api.Test;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

import static com.fdpro.apps.stockwallet.WalletTestUtils.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TransactionTest {

    @Test
    void buy_NegativeUnits() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.buy(-1, SYMBOL));
    }

    @Test
    void buy_ZeroUnits() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.buy(0, SYMBOL));
    }

    @Test
    void buy_NullSymbol() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.buy(1, null));
    }

    @Test
    void sell_NegativeUnits() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.sell(-1, SYMBOL));
    }

    @Test
    void sell_ZeroUnits() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.sell(0, SYMBOL));
    }

    @Test
    void sell_NullSymbol() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.sell(1, null));
    }

    @Test
    void atPrice_Null() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.buy(1, SYMBOL).atPrice(null));
    }

    @Test
    void atPrice_Negative() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.buy(1, SYMBOL).atPrice(EURO_AMOUNT.negate()));
    }

    @Test
    void atPrice_Zero() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.buy(1, SYMBOL).atPrice(ZERO_EUROS));
    }

    @Test
    void forCost_Null() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.buy(1, SYMBOL).forCost(null));
    }

    @Test
    void forCost_Negative() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.buy(1, SYMBOL).forCost(EURO_AMOUNT.negate()));
    }

    @Test
    void build_NoPrice() {
        assertThrows(IllegalArgumentException.class, () -> Transaction.buy(1, SYMBOL).build());
    }

    @Test
    void build_DifferentCurrencies() {
        assertThrows(IllegalArgumentException.class, () -> {
            Money cost = Money.of(BigDecimal.valueOf(1), DOLLARS);
            Transaction.buy(1, SYMBOL)
              .atPrice(EURO_AMOUNT)
              .forCost(cost)
              .build();
        });
    }

    @Test
    void amount_Buy() {
        Transaction transaction = Transaction.buy(2, SYMBOL)
          .atPrice(EURO_AMOUNT)
          .forCost(EURO_AMOUNT)
          .build();

        MonetaryAmount expectedResult = Money.of(BigDecimal.valueOf(4.5), EURO).negate();
        assertEquals(expectedResult, transaction.amount());
    }

    @Test
    void amount_Sell() {
        Transaction transaction = Transaction.sell(2, SYMBOL)
          .atPrice(EURO_AMOUNT)
          .forCost(EURO_AMOUNT)
          .build();

        assertEquals(EURO_AMOUNT, transaction.amount());
    }

    @Test
    void amount_NoCost() {
        Transaction transaction = Transaction.sell(2, SYMBOL)
          .atPrice(EURO_AMOUNT)
          .build();

        MonetaryAmount expectedResult = Money.of(BigDecimal.valueOf(3.0), EURO);
        assertEquals(expectedResult, transaction.amount());
    }

    @Test
    void count_Buy() {
        int units = 2;
        Transaction transaction = Transaction.buy(units, SYMBOL)
          .atPrice(EURO_AMOUNT)
          .forCost(EURO_AMOUNT)
          .build();

        assertEquals(units, transaction.count());
    }

    @Test
    void count_Sell() {
        int units = 2;
        Transaction transaction = Transaction.sell(units, SYMBOL)
          .atPrice(EURO_AMOUNT)
          .forCost(EURO_AMOUNT)
          .build();

        assertEquals(- units, transaction.count());
    }
}