package com.fdpro.apps.stockwallet.symbol.repositories;

import com.fdpro.apps.stockwallet.symbol.domain.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolRepository extends JpaRepository<Symbol, String> {
}
