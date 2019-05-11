package com.fdpro.apps.stockwallet.symbol.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Objects;

@Entity
@JsonRootName("symbol")
public class Symbol {
    @Id
    @JsonProperty
    private String code;

    @Column(nullable = false)
    @JsonProperty
    private String name;

    private Symbol() {}

    public Symbol(String code, String name) {
        Assert.hasText(code, "Code must contain some significant characters");
        Assert.hasText(name, "Name must contain some significant characters");

        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Symbol symbol = (Symbol) o;
        return code.equals(symbol.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
}
