package com.fdpro.apps.stockwallet.wallet.ws;

import com.fdpro.apps.stockwallet.symbol.repositories.SymbolRepository;
import com.fdpro.apps.stockwallet.wallet.domain.CashFlow;
import com.fdpro.apps.stockwallet.symbol.domain.Symbol;
import com.fdpro.apps.stockwallet.wallet.domain.Transaction;
import com.fdpro.apps.stockwallet.wallet.domain.Wallet;
import com.fdpro.apps.stockwallet.wallet.repositories.WalletRepository;
import com.fdpro.apps.stockwallet.util.JsonUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.fdpro.apps.stockwallet.wallet.WalletTestUtils.EURO_AMOUNT;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class WalletServiceIT {
    private static final String WALLET_NAME = "My wallet";

    private Wallet wallet;

    @Autowired
    private SymbolRepository symbolRepository;
    @Autowired
    private WalletRepository walletRepository;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    private void beforeEach() {
        Symbol symbol = new Symbol("ABI", "AB Inbev");
        symbolRepository.save(symbol);

        wallet = new Wallet(WALLET_NAME);
        wallet.register(CashFlow.deposit(EURO_AMOUNT));
        wallet.register(CashFlow.deposit(EURO_AMOUNT));
        wallet.register(CashFlow.deposit(EURO_AMOUNT));
        wallet.register(Transaction.buy(2, symbol).atPrice(EURO_AMOUNT).build());
        wallet.register(Transaction.sell(1, symbol).atPrice(EURO_AMOUNT).build());
        walletRepository.saveAndFlush(wallet);
    }

    @Test
    void getWalletForName() throws Exception {
        MvcResult mvcResult = mockMvc
          .perform(
            get("/wallets/" + wallet.getName())
              .accept(MediaType.APPLICATION_JSON_UTF8_VALUE)
          )
          .andExpect(status().isOk())
          .andReturn();

        Wallet result = JsonUtil.fromJson(mvcResult.getResponse().getContentAsString(), Wallet.class);

        assertThat(result, equalTo(wallet));
    }

    @Test
    void saveWallet() throws Exception {
        String json = JsonUtil.toJson(wallet);

        mockMvc
          .perform(
            post("/wallets/")
              .contentType(MediaType.APPLICATION_JSON_UTF8)
              .content(json)
          )
          .andExpect(status().isOk());
    }
}