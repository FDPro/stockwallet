package com.fdpro.apps.stockwallet.wallet.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fdpro.apps.stockwallet.util.orm.MonetaryAmountConverter;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a deposit or withdrawal into a wallet
 *
 * @author fdpro
 */
@Entity(name = "CASH_FLOW")
@JsonRootName("cashFlow")
public class CashFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty
    private Long id;

    @Column(nullable = false)
    @JsonProperty
    private LocalDate date;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private CashFlowType type;

    @Column(nullable = false)
    @Convert(converter = MonetaryAmountConverter.class)
    @JsonProperty
    private MonetaryAmount amount;

    /**
     * Creates a deposit cash flow
     *
     * @param date the date of the deposit
     * @param amount the amount of the deposit
     * @return a deposit {@link CashFlow} of {@code amount} on a given {@code date}
     * @throws IllegalArgumentException when {@code date} is {@literal null}
     * @throws IllegalArgumentException when {@code amount} is {@literal null}
     * @throws IllegalArgumentException when {@code amount} is lesser than or equals to 0
     */
    public static CashFlow deposit(LocalDate date, MonetaryAmount amount) {
        Assert.notNull(date, "Date mustn't be null");
        Assert.notNull(amount, "Amount mustn't be null");
        Assert.isTrue(amount.isPositive(), "Amount must be greater than 0");

        return new CashFlow(CashFlowType.DEPOSIT, date, amount);
    }

    /**
     * Creates a withdrawal cash flow
     *
     * @param date the date of the withdrawal
     * @param amount the amount of the withdrawal
     * @return a withdrawal {@link CashFlow} of {@code amount} on a given {@code date}
     * @throws IllegalArgumentException when {@code date} is {@literal null}
     * @throws IllegalArgumentException when {@code amount} is {@literal null}
     * @throws IllegalArgumentException when {@code amount} is lesser than or equals to 0
     */
    public static CashFlow withdrawal(LocalDate date, MonetaryAmount amount) {
        Assert.notNull(date, "Date mustn't be null");
        Assert.notNull(amount, "Amount mustn't be null");
        Assert.isTrue(amount.isPositive(), "Amount must be greater than 0");

        return new CashFlow(CashFlowType.WITHDRAWAL, date, amount);
    }

    private CashFlow() {}

    private CashFlow(CashFlowType type, LocalDate date, MonetaryAmount amount) {
        this.type = type;
        this.date = date;
        this.amount = amount;
    }

    public Long getId() {
        return id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CashFlow cashFlow = (CashFlow) o;
        return Objects.equals(id, cashFlow.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private enum CashFlowType {
        DEPOSIT,
        WITHDRAWAL
    }
}
