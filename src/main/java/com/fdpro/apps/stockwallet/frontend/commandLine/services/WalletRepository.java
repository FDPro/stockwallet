package com.fdpro.apps.stockwallet.frontend.commandLine.services;

import com.fdpro.apps.stockwallet.backend.wallet.domain.Wallet;

import java.util.Optional;

public interface WalletRepository {
    /**
     * Gives a wallet matching the {@code name}
     * @param name the wallet's name
     * @return
     */
    Optional<Wallet> findWalletByName(String name);
}
