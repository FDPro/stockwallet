package com.fdpro.apps.stockwallet.domain;

import org.javamoney.moneta.Money;
import org.springframework.util.Assert;

import javax.money.MonetaryAmount;
import java.math.BigDecimal;

/**
 * Represents a {@link Symbol} buy or sell in a wallet
 *
 * @author fdpro
 */
public class Transaction {
    private TransactionType type;
    private Symbol symbol;
    private int units;
    private MonetaryAmount unitPrice;
    private MonetaryAmount transactionCost;

    public static Builder buy(int units, Symbol symbol) {
        Assert.isTrue(units > 0, "Units must be greater than 0");
        Assert.notNull(symbol, "Symbol mustn't be null");

        return new Builder(TransactionType.BUY, units, symbol);
    }

    public static Builder sell(int units, Symbol symbol) {
        Assert.isTrue(units > 0, "Units must be greater than 0");
        Assert.notNull(symbol, "Symbol mustn't be null");

        return new Builder(TransactionType.SELL, units, symbol);
    }

    private Transaction(Builder builder) {
        type = builder.type;
        symbol = builder.symbol;
        units = builder.units;
        unitPrice = builder.unitPrice;
        transactionCost = builder.transactionCost;
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

    private enum TransactionType {
        BUY,
        SELL
    }

    public static final class Builder {
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
