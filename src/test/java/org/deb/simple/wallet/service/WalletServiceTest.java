package org.deb.simple.wallet.service;

import static org.junit.jupiter.api.Assertions.*;

import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WalletServiceTest {

  @Autowired WalletServiceImpl walletService;

  @Test
  void createWallet() {
    Integer[] coins = {2, 3, 1, 2, 1};
    CreateWalletResponse createWalletResponse = walletService.createWallet(coins);
    assertNotNull(createWalletResponse.getWalletId());
    assertEquals(0, createWalletResponse.getWalletErrorList().size());
  }

  @Test
  void createWalletError() {
    Integer[] coins = {1, 2, 3, -1, 0};
    CreateWalletResponse createWalletResponse = walletService.createWallet(coins);
    assertNotNull(createWalletResponse.getWalletId());
    assertEquals(2, createWalletResponse.getWalletErrorList().size());
    assertEquals(
        "Coin value ('0') should be greater than zero.",
        createWalletResponse.getWalletErrorList().get(0).getErrorMessage());
    assertEquals(
        "Coin value ('-1') should be greater than zero.",
        createWalletResponse.getWalletErrorList().get(1).getErrorMessage());
  }
}
