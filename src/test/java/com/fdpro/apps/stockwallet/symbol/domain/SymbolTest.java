package com.fdpro.apps.stockwallet.symbol.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class SymbolTest {
    @Test
    void newSymbol_NullCode() {
        assertThrows(IllegalArgumentException.class, () -> new Symbol(null, "Ion Beam Application"));
    }

    @Test
    void newSymbol_EmptyCode() {
        assertThrows(IllegalArgumentException.class, () -> new Symbol("", "Ion Beam Application"));
    }

    @Test
    void newSymbol_BlankCode() {
        assertThrows(IllegalArgumentException.class, () -> new Symbol("   ", "Ion Beam Application"));
    }

    @Test
    void newSymbol_NullName() {
        assertThrows(IllegalArgumentException.class, () -> new Symbol("IBA", null));
    }

    @Test
    void newSymbol_EmptyName() {
        assertThrows(IllegalArgumentException.class, () -> new Symbol("IBA", ""));
    }

    @Test
    void newSymbol_BlankName() {
        assertThrows(IllegalArgumentException.class, () -> new Symbol("IBA", "   "));
    }
}