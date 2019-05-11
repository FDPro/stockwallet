package com.fdpro.apps.stockwallet.wallet.ws;

import com.fdpro.apps.stockwallet.wallet.domain.Wallet;
import com.fdpro.apps.stockwallet.wallet.repositories.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class WalletService {
    private WalletRepository repository;

    @Autowired
    public WalletService(WalletRepository repository) {
        this.repository = repository;
    }

    @GetMapping(value = "/wallets/{name}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody Wallet getWalletForName(@PathVariable("name") String name) {
        return repository.findById(name)
          .orElseThrow(() -> new IllegalArgumentException("Name should match an existing wallet"));
    }

    @PostMapping(value = "/wallets", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void saveWallet(@RequestBody Wallet wallet) {
        repository.save(wallet);
    }
}
