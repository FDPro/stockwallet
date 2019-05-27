package com.fdpro.apps.stockwallet.backend.wallet.domain;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static com.fdpro.apps.stockwallet.backend.wallet.WalletTestUtils.EURO_AMOUNT;
import static com.fdpro.apps.stockwallet.backend.wallet.WalletTestUtils.ZERO_EUROS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CashFlowTest {
    private static final LocalDate DATE = LocalDate.now();

    @Test
    void deposit_NullDate() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.deposit(null, EURO_AMOUNT));
    }

    @Test
    void deposit_NullAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.deposit(DATE, null));
    }

    @Test
    void deposit_NegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.deposit(DATE, EURO_AMOUNT.negate()));
    }

    @Test
    void deposit_ZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.deposit(DATE, ZERO_EUROS));
    }

    @Test
    void withdrawal_NullDate() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.withdrawal(null, EURO_AMOUNT));
    }

    @Test
    void withdrawal_NullAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.withdrawal(DATE, null));
    }

    @Test
    void withdrawal_NegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.withdrawal(DATE, EURO_AMOUNT.negate()));
    }

    @Test
    void withdrawal_ZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.withdrawal(DATE, ZERO_EUROS));
    }

    @Test
    void amount_Deposit() {
        CashFlow cashFlow = CashFlow.deposit(DATE, EURO_AMOUNT);
        assertEquals(EURO_AMOUNT, cashFlow.amount());
    }

    @Test
    void amount_Withdrawal() {
        CashFlow cashFlow = CashFlow.withdrawal(DATE, EURO_AMOUNT);
        assertEquals(EURO_AMOUNT.negate(), cashFlow.amount());
    }
}