package com.fdpro.apps.stockwallet.repositories;

import com.fdpro.apps.stockwallet.domain.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

/**
 * {@link Repository} managing {@link Wallet}
 *
 * @author fdpro
 */
public interface WalletRepository extends JpaRepository<Wallet, String> {

}
