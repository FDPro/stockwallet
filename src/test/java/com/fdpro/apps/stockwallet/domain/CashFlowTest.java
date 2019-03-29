package com.fdpro.apps.stockwallet.domain;

import org.junit.jupiter.api.Test;

import static com.fdpro.apps.stockwallet.WalletTestUtils.EURO_AMOUNT;
import static com.fdpro.apps.stockwallet.WalletTestUtils.ZERO_EUROS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CashFlowTest {

    @Test
    void deposit_NullAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.deposit(null));
    }

    @Test
    void deposit_NegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.deposit(EURO_AMOUNT.negate()));
    }

    @Test
    void deposit_ZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.deposit(ZERO_EUROS));
    }

    @Test
    void withdrawal_NullAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.withdrawal(null));
    }

    @Test
    void withdrawal_NegativeAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.withdrawal(EURO_AMOUNT.negate()));
    }

    @Test
    void withdrawal_ZeroAmount() {
        assertThrows(IllegalArgumentException.class, () -> CashFlow.withdrawal(ZERO_EUROS));
    }

    @Test
    void amount_Deposit() {
        CashFlow cashFlow = CashFlow.deposit(EURO_AMOUNT);
        assertEquals(EURO_AMOUNT, cashFlow.amount());
    }

    @Test
    void amount_Withdrawal() {
        CashFlow cashFlow = CashFlow.withdrawal(EURO_AMOUNT);
        assertEquals(EURO_AMOUNT.negate(), cashFlow.amount());
    }
}