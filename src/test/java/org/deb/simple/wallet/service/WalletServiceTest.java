package org.deb.simple.wallet.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.deb.simple.wallet.dto.GetWalletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class WalletServiceTest {

  private static final UUID NON_EXISTING_WALLET_ID =
      UUID.fromString("6b7b24f5-7d77-4bf8-a085-5c967f24f4e0");
  private final Integer[] coins = {2, 3, 1, 2, 1};
  @Autowired WalletServiceImpl walletService;

  @Test
  void createWallet() {

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

  @Test
  void getWallet() {
    CreateWalletResponse createWalletResponse = walletService.createWallet(coins);
    UUID walletId = createWalletResponse.getWalletId();
    GetWalletResponse getWalletResponse = walletService.getWallet(walletId);
    assertEquals("current coins are [1, 1, 2, 2, 3]", getWalletResponse.getMessage());
  }

  @Test
  void getNonExistingWallet() {
    GetWalletResponse getWalletResponse = walletService.getWallet(NON_EXISTING_WALLET_ID);
    assertNull(getWalletResponse.getMessage());
    assertEquals(
        String.format("'%s' matching wallet not found", NON_EXISTING_WALLET_ID),
        getWalletResponse.getErrors().get(0).getErrorMessage());
  }
}
