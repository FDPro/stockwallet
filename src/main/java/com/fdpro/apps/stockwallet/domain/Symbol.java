package com.fdpro.apps.stockwallet.domain;

import org.springframework.util.Assert;

public class Symbol {
    private String code;
    private String name;

    public Symbol(String code, String name) {
        Assert.hasText(code, "Code must contain some significant characters");
        Assert.hasText(name, "Name must contain some significant characters");

        this.code = code;
        this.name = name;
    }
}
