package org.deb.simple.wallet.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.deb.simple.wallet.dto.GetWalletResponse;
import org.deb.simple.wallet.dto.PayResponse;
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

  @Test
  void payExact() {
    Integer[] coins = {2, 3, 1, 2, 1};
    CreateWalletResponse response = walletService.createWallet(coins);
    UUID walletId = response.getWalletId();
    payNVerify(walletId, 1, "Successfully paid 1 current coins are [1, 2, 2, 3]");
  }

  @Test
  void payExactTwice() {
    Integer[] coins = {2, 3, 1, 2, 1};
    CreateWalletResponse response = walletService.createWallet(coins);
    UUID walletId = response.getWalletId();
    payNVerify(walletId, 1, "Successfully paid 1 current coins are [1, 2, 2, 3]");
    payNVerify(walletId,3,"Successfully paid 3 current coins are [2, 3]");
  }
  @Test
  void payWithChange() {
    Integer[] coins = {2, 3, 1, 2, 1};
    CreateWalletResponse response = walletService.createWallet(coins);
    UUID walletId = response.getWalletId();
    payNVerify(walletId, 1, "Successfully paid 1 current coins are [1, 2, 2, 3]");
    payNVerify(walletId,3,"Successfully paid 3 current coins are [2, 3]");
    payNVerify(walletId,1,"Successfully paid 1 current coins are [1, 3]");
  }

  @Test
  void payWithChangeTwice() {
    Integer[] coins = {2, 3, 1, 2, 1};
    CreateWalletResponse response = walletService.createWallet(coins);
    UUID walletId = response.getWalletId();
    payNVerify(walletId, 1, "Successfully paid 1 current coins are [1, 2, 2, 3]");
    payNVerify(walletId, 3, "Successfully paid 3 current coins are [2, 3]");
    payNVerify(walletId, 1, "Successfully paid 1 current coins are [1, 3]");
    payNVerify(walletId, 2, "Successfully paid 2 current coins are [2]");
  }

  @Test
  void payMoreThanYouHave() {
    Integer[] coins = {2, 3, 1, 2, 1};
    CreateWalletResponse response = walletService.createWallet(coins);
    UUID walletId = response.getWalletId();
    payNVerify(walletId, 1, "Successfully paid 1 current coins are [1, 2, 2, 3]");
    payNVerify(walletId, 3, "Successfully paid 3 current coins are [2, 3]");
    payNVerify(walletId, 1, "Successfully paid 1 current coins are [1, 3]");
    payNVerify(walletId, 2, "Successfully paid 2 current coins are [2]");
    payNVerify(walletId, 5, "You do not have sufficient coins to pay 5 current coins are [2]");

  }

  @Test
  void payMarginalMore() {
    Integer[] coins = {2, 3, 1, 2, 1};
    CreateWalletResponse response = walletService.createWallet(coins);
    UUID walletId = response.getWalletId();
    payNVerify(walletId, 10, "You do not have sufficient coins to pay 10 current coins are [1, 1, 2, 2, 3]");
  }

  @Test
  void payMarginalExact() {
    Integer[] coins = {2, 3, 1, 2, 1};
    CreateWalletResponse response = walletService.createWallet(coins);
    UUID walletId = response.getWalletId();
    payNVerify(walletId, 9, "Successfully paid 9 current coins are []");
  }

  @Test
  void payWithNonExistingWallet() {
    PayResponse payResponse =
        walletService.pay(NON_EXISTING_WALLET_ID, 100);
    assertEquals(1, payResponse.getErrors().size());
    assertEquals(
        String.format("'%s' matching wallet not found",NON_EXISTING_WALLET_ID),
        payResponse.getErrors().get(0).getErrorMessage());
  }

  private void payNVerify(UUID walletId, int amount, String messageToVerify) {
    PayResponse payResponse = walletService.pay(walletId, amount);
    assertEquals(0, payResponse.getErrors().size());
    assertEquals(messageToVerify, payResponse.getMessage());
  }
}
