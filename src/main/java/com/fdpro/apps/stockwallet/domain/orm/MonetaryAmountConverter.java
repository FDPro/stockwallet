package com.fdpro.apps.stockwallet.domain.orm;

import org.javamoney.moneta.Money;

import javax.money.MonetaryAmount;
import javax.money.format.MonetaryParseException;
import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Locale;

/**
 * {@link AttributeConverter} for {@link MonetaryAmount}
 *
 * @author fdpro
 */
@Converter
public class MonetaryAmountConverter implements AttributeConverter<MonetaryAmount, String> {
    @Override
    public String convertToDatabaseColumn(MonetaryAmount attribute) {
        if (attribute == null) {
            return null;
        }
        return format(attribute);
    }

    /**
     * {@inheritDoc}
     * @throws MonetaryParseException when {@code dbData} doesn't respect the default {@link Money} format
     */
    @Override
    public MonetaryAmount convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return Money.parse(dbData);
    }

    private String format(MonetaryAmount monetaryAmount) {
        return String.format(Locale.ROOT, "%s %f", monetaryAmount.getCurrency().getCurrencyCode(), monetaryAmount.getNumber().doubleValue());
    }
}
