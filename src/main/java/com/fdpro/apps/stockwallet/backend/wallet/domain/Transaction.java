package com.fdpro.apps.stockwallet.backend.wallet.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fdpro.apps.stockwallet.backend.symbol.domain.Symbol;
import com.fdpro.apps.stockwallet.backend.util.orm.MonetaryAmountConverter;
import org.javamoney.moneta.Money;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Represents a {@link Symbol} buy or sell in a wallet
 *
 * @author fdpro
 */
@Entity(name = "TRANSACTON")
@JsonRootName("transaction")
public class Transaction {
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
    private TransactionType type;

    @ManyToOne
    @JsonProperty
    private Symbol symbol;

    @Column(nullable = false)
    @JsonProperty
    private int units;

    @Column(nullable = false)
    @Convert(converter = MonetaryAmountConverter.class)
    @JsonProperty
    private MonetaryAmount unitPrice;

    @Column(nullable = false)
    @Convert(converter = MonetaryAmountConverter.class)
    @JsonProperty
    private MonetaryAmount transactionCost;

    /**
     * Creates a buying transaction builder for x units of a symbol
     * @param units the units
     * @param symbol the symbol
     * @return a {@link Builder} of a buying {@link Transaction} of {@code units} units of {@code symbol}
     * @throws IllegalArgumentException when {@code units} is lesser or equal to 0
     * @throws IllegalArgumentException when {@code symbol} is {@literal null}
     */
    public static Builder buy(int units, Symbol symbol) {
        Assert.isTrue(units > 0, "Units must be greater than 0");
        Assert.notNull(symbol, "Symbol mustn't be null");

        return new Builder(TransactionType.BUY, units, symbol);
    }

    /**
     * Creates a selling transaction builder for x units of a symbol
     * @param units the units
     * @param symbol the symbol
     * @return a {@link Builder} of a selling {@link Transaction} of {@code units} units of {@code symbol}
     * @throws IllegalArgumentException when {@code units} is lesser or equal to 0
     * @throws IllegalArgumentException when {@code symbol} is {@literal null}
     */
    public static Builder sell(int units, Symbol symbol) {
        Assert.isTrue(units > 0, "Units must be greater than 0");
        Assert.notNull(symbol, "Symbol mustn't be null");

        return new Builder(TransactionType.SELL, units, symbol);
    }

    private Transaction() {}

    private Transaction(Builder builder) {
        type = builder.type;
        date = builder.date;
        symbol = builder.symbol;
        units = builder.units;
        unitPrice = builder.unitPrice;
        transactionCost = builder.transactionCost;
    }

    public Long getId() {
        return id;
    }

    public Symbol getSymbol() {
        return symbol;
    }

    /**
     * Gives the monetary amount of the transaction,
     * that is the amount that went in or out a wallet.
     * The amount is positive if the transaction adds money to the wallet, negative otherwise.
     *
     * @return the monetary amount of {@code this}
     */
    public MonetaryAmount amount() {
        return totalPrice().multiply(amountSign()).subtract(transactionCost);
    }

    private MonetaryAmount totalPrice() {
        return unitPrice.multiply(units);
    }

    public int count() {
        return  units * symbolSign();
    }

    private int symbolSign() {
        return type.equals(TransactionType.BUY) ? 1 : -1;
    }

    private int amountSign() {
        return - symbolSign();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private enum TransactionType {
        BUY,
        SELL
    }

    public static final class Builder {
        private LocalDate date;
        private TransactionType type;
        private Symbol symbol;
        private int units;
        private MonetaryAmount unitPrice;
        private MonetaryAmount transactionCost;

        private Builder(TransactionType type, int units, Symbol symbol) {
            this.type = type;
            this.units = units;
            this.symbol = symbol;
        }

        public Builder on(LocalDate date) {
            Assert.notNull(date, "Date mustn't be null");

            this.date = date;
            return this;
        }

        public Builder atPrice(MonetaryAmount price) {
            Assert.notNull(price, "Price mustn't be null");
            Assert.isTrue(price.isPositive(), "Price must be greater than 0");

            unitPrice = price;
            return this;
        }

        public Builder forCost(MonetaryAmount cost) {
            Assert.notNull(cost, "Cost mustn't be null");
            Assert.isTrue(cost.isPositiveOrZero(), "Price must be greater than or equal to 0");

            transactionCost = cost;
            return this;
        }

        public Transaction build() {
            Assert.notNull(date, "The transaction must have occur on a date");
            Assert.notNull(unitPrice, "The transaction must have a price");

            if (transactionCost == null) {
                transactionCost = Money.of(BigDecimal.ZERO, unitPrice.getCurrency());
            }

            Assert.isTrue(
              unitPrice.getCurrency().equals(transactionCost.getCurrency()),
              "The transaction price and cost must have the same currency"
            );

            return new Transaction(this);
        }
    }
}
