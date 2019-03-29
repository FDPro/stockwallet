package com.fdpro.apps.stockwallet.domain;

import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

/**
 * Represents a deposit or withdrawal into a wallet
 *
 * @author fdpro
 */
public class CashFlow {
    private CashFlowType type;
    private MonetaryAmount amount;

    /**
     * Creates a deposit cash flow
     *
     * @param amount the amount of the deposit
     * @return a deposit {@link CashFlow} of {@code amount}
     * @throws IllegalArgumentException when {@code amount} is {@literal null}
     * @throws IllegalArgumentException when {@code amount} is lesser than or equals to 0
     */
    public static CashFlow deposit(MonetaryAmount amount) {
        Assert.notNull(amount, "Amount mustn't be null");
        Assert.isTrue(amount.isPositive(), "Amount must be greater than 0");

        return new CashFlow(CashFlowType.DEPOSIT, amount);
    }

    /**
     * Creates a withdrawal cash flow
     *
     * @param amount the amount of the withdrawal
     * @return a withdrawal {@link CashFlow} of {@code amount}
     * @throws IllegalArgumentException when {@code amount} is {@literal null}
     * @throws IllegalArgumentException when {@code amount} is lesser than or equals to 0
     */
    public static CashFlow withdrawal(MonetaryAmount amount) {
        Assert.notNull(amount, "Amount mustn't be null");
        Assert.isTrue(amount.isPositive(), "Amount must be greater than 0");

        return new CashFlow(CashFlowType.WITHDRAWAL, amount);
    }

    private CashFlow(CashFlowType type, MonetaryAmount amount) {
        this.type = type;
        this.amount = amount;
    }

    /**
     * Gives the monetary amount of the cash flow,
     * that is the amount that went in or out a wallet.
     * The amount is positive if the cash flow adds money to a wallet, negative otherwise.
     *
     * @return the monetary amount of {@code this}
     */
    public MonetaryAmount amount() {
        return amount.multiply(signum());
    }

    private BigDecimal signum() {
        BigDecimal one = BigDecimal.ONE;
        return type.equals(CashFlowType.DEPOSIT) ? one : one.negate();
    }

    private enum CashFlowType {
        DEPOSIT,
        WITHDRAWAL
    }
}
