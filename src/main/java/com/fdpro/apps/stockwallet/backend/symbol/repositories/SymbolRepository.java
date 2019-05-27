package com.fdpro.apps.stockwallet.backend.symbol.repositories;

import com.fdpro.apps.stockwallet.backend.symbol.domain.Symbol;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SymbolRepository extends JpaRepository<Symbol, String> {
}
