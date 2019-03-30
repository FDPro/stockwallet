package com.fdpro.apps.stockwallet.domain;

import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Symbol {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(nullable = false)
    private String code;
    @Column(nullable = false)
    private String name;

    public Symbol(String code, String name) {
        Assert.hasText(code, "Code must contain some significant characters");
        Assert.hasText(name, "Name must contain some significant characters");

        this.code = code;
        this.name = name;
    }

    public Long getId() {
        return id;
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
        return Objects.equals(id, symbol.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
