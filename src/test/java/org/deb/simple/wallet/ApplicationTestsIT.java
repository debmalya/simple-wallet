package org.deb.simple.wallet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.deb.simple.wallet.controller.WalletController;
import org.deb.simple.wallet.dto.CreateWalletRequest;
import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class ApplicationTestsIT {

  @LocalServerPort private int port;

  @Autowired private TestRestTemplate restTemplate;

  @Autowired private WalletController walletController;

  @Test
  void contextLoads() {
    assertNotNull(walletController);
  }

  @Test
  public void createWallet() throws Exception {
    CreateWalletRequest createWalletRequest = new CreateWalletRequest();
    createWalletRequest.setCoins(new Integer[] {2, 3, 1, 2, 1});
    assertThat(
        this.restTemplate
            .postForEntity(
                String.format("http://localhost:%d/api/wallet/v0/create", port),
                createWalletRequest,
                CreateWalletResponse.class)
            .getStatusCode()
            .equals(HttpStatus.CREATED));
  }
}
