package com.fdpro.apps.stockwallet.frontend.commandLine.commands;

import static picocli.CommandLine.*;

@Command(
  name = "worth"
)
public class WorthCommand implements Runnable {
    @ParentCommand
    private WalletCommand walletCommand;

    @Override
    public void run() {

    }
}
