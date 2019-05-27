package com.fdpro.apps.stockwallet.frontend.commandLine.commands;

import static picocli.CommandLine.*;

@Command(
  name = "wallet",
  subcommands = {
    WorthCommand.class
  }
)
public class WalletCommand {
    @Option(names = {"-n", "--name"})
    private String name;

    public String getName() {
        return name;
    }
}
