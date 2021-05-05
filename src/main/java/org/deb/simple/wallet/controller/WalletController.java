package org.deb.simple.wallet.controller;

import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.deb.simple.wallet.dto.CreateWalletRequest;
import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.deb.simple.wallet.dto.GetWalletRequest;
import org.deb.simple.wallet.dto.GetWalletResponse;
import org.deb.simple.wallet.dto.PayRequest;
import org.deb.simple.wallet.dto.PayResponse;
import org.deb.simple.wallet.service.WalletServiceImpl;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wallet/v0")
public class WalletController {

  private final WalletServiceImpl walletService;

  /**
   * Creates a wallet.
   *
   * @param createWalletRequest wallet creation request.
   * @return created wallet id.
   */
  @PostMapping(value = "/create")
  public ResponseEntity<CreateWalletResponse> createWallet(
      @RequestBody CreateWalletRequest createWalletRequest) {
    CreateWalletResponse createWalletResponse =
        walletService.createWallet(createWalletRequest.getCash());
    return ResponseEntity.status(HttpStatus.CREATED).body(createWalletResponse);
  }

  /**
   * Get wallet by wallet id.
   *
   * @param walletId to search.
   * @return current cash details.
   */
  @GetMapping(value = "/get/{walletId}")
  public ResponseEntity<GetWalletResponse> getWallet(
      @PathVariable UUID walletId) {
    GetWalletResponse getWalletResponse = walletService.getWallet(walletId);
    if (Objects.nonNull(getWalletResponse.getWalletId())) {
      return ResponseEntity.status(HttpStatus.OK).body(getWalletResponse);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getWalletResponse);
    }
  }

  /**
   * To pay using wallet.
   *
   * @param payRequest contains wallet id and amount to be paid.
   * @return whether payment is successful or not and current coin status.
   */
  @PutMapping(value = "/pay")
  public ResponseEntity<PayResponse> pay(@RequestBody PayRequest payRequest) {
    PayResponse payWalletResponse =
        walletService.pay(payRequest.getWalletId(), payRequest.getAmount());
    return ResponseEntity.status(HttpStatus.OK).body(payWalletResponse);
  }
}
