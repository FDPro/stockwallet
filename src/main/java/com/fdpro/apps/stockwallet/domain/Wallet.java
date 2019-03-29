package com.fdpro.apps.stockwallet.domain;

import org.javamoney.moneta.Money;
import org.springframework.util.Assert;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a wallet
 *
 * @author fdpro
 */
public class Wallet {
    private String name;
    private List<CashFlow> cashFlows;
    private List<Transaction> transactions;

    public Wallet(String name) {
        Assert.hasText(name, "The name must contain some significant characters");

        this.name = name;
        this.cashFlows = new ArrayList<>();
        this.transactions = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    /**
     * Registers a cash flow on the wallet
     *
     * @param cashFlow the cash flow
     * @throws IllegalArgumentException when {@code cashFlow} is {@literal null}
     */
    public void register(CashFlow cashFlow) {
        Assert.notNull(cashFlow, "Cash flow mustn't be null");

        cashFlows.add(cashFlow);
    }

    /**
     * Registers a transaction on the wallet
     *
     * @param transaction the transaction
     * @throws IllegalArgumentException when {@code transaction} is {@literal null}
     */
    public void register(Transaction transaction) {
        Assert.notNull(transaction, "Transaction mustn't be null");

        transactions.add(transaction);
    }

    /**
     * Gives the amount of a currency in the wallet
     *
     * @param currency the currency
     * @return the amount of {@code currency} in {@code this}
     * @throws IllegalArgumentException when currency is {@literal null}
     */
    public MonetaryAmount amount(CurrencyUnit currency) {
        Assert.notNull(currency, "Currency mustn't be null");

        return cashFlows.stream()
          .map(CashFlow::amount)
          .filter(amount -> amount.getCurrency().equals(currency))
          .reduce(Money.zero(currency), MonetaryAmount::add);
    }

    /**
     * Gives the count of a symbol in the wallet
     *
     * @param symbol the symbol
     * @return the count of {@code symbol} in {@code this}
     * @throws IllegalArgumentException when {@code symbol} is {@literal null}
     */
    public int count(Symbol symbol) {
        Assert.notNull(symbol, "Currency mustn't be null");

        return transactions.stream()
          .filter(transaction -> transaction.getSymbol().equals(symbol))
          .mapToInt(Transaction::count)
          .sum();
    }
}
