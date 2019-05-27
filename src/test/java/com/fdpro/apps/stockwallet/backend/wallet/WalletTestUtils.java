package com.fdpro.apps.stockwallet.backend.wallet;

import com.fdpro.apps.stockwallet.backend.symbol.domain.Symbol;
import org.javamoney.moneta.Money;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import java.math.BigDecimal;

public class WalletTestUtils {
    public static final CurrencyUnit EURO = Monetary.getCurrency("EUR");
    public static final CurrencyUnit DOLLARS = Monetary.getCurrency("USD");
    public static final Money ZERO_EUROS = Money.of(BigDecimal.ZERO, EURO);
    public static final Money ZERO_DOLLARS = Money.of(BigDecimal.ZERO, DOLLARS);
    public static final Money EURO_AMOUNT = Money.of(1.5, EURO);

    public static final Symbol SYMBOL = new Symbol("IBA", "Ion Beam Application");
}
