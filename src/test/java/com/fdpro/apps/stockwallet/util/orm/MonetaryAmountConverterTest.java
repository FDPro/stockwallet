package com.fdpro.apps.stockwallet.util.orm;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.money.format.MonetaryParseException;

import static com.fdpro.apps.stockwallet.wallet.WalletTestUtils.EURO_AMOUNT;
import static org.junit.jupiter.api.Assertions.*;

class MonetaryAmountConverterTest {
    private static final String EURO_AMOUNT_VALUE = "EUR 1.500000";
    private static final String EURO_AMOUNT_VALUE_WRONG_FORMAT = "1.5-EUR";

    private MonetaryAmountConverter converter;

    @BeforeEach
    private void beforeEach() {
        converter = new MonetaryAmountConverter();
    }

    @Test
    void convertToDatabaseColumn_Null() {
        assertNull(converter.convertToDatabaseColumn(null));
    }

    @Test
    void convertToDatabaseColumn() {
        assertEquals(EURO_AMOUNT_VALUE, converter.convertToDatabaseColumn(EURO_AMOUNT));
    }

    @Test
    void convertToEntityAttribute_Null() {
        assertNull(converter.convertToEntityAttribute(null));
    }

    @Test
    void convertToEntityAttribute_WrongFormat() {
        assertThrows(MonetaryParseException.class, () -> converter.convertToEntityAttribute(EURO_AMOUNT_VALUE_WRONG_FORMAT));
    }

    @Test
    void convertToEntityAttribute() {
        assertEquals(EURO_AMOUNT, converter.convertToEntityAttribute(EURO_AMOUNT_VALUE));
    }
}