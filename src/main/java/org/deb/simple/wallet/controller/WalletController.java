package org.deb.simple.wallet.controller;

import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.deb.simple.wallet.dto.CreateWalletRequest;
import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.deb.simple.wallet.dto.GetWalletRequest;
import org.deb.simple.wallet.dto.GetWalletResponse;
import org.deb.simple.wallet.dto.PayRequest;
import org.deb.simple.wallet.dto.PayResponse;
import org.deb.simple.wallet.service.WalletServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping(value = "/create")
  public ResponseEntity<CreateWalletResponse> createWallet(
      @RequestBody CreateWalletRequest createWalletRequest) {
    CreateWalletResponse createWalletResponse =
        walletService.createWallet(createWalletRequest.getCoins());
    return ResponseEntity.status(HttpStatus.CREATED).body(createWalletResponse);
  }

  @GetMapping(value = "/get")
  public ResponseEntity<GetWalletResponse> getWallet(
      @RequestBody GetWalletRequest getWalletRequest) {
    GetWalletResponse getWalletResponse = walletService.getWallet(getWalletRequest.getWalletId());
    if (Objects.nonNull(getWalletResponse.getWalletId())) {
      return ResponseEntity.status(HttpStatus.FOUND).body(getWalletResponse);
    } else {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(getWalletResponse);
    }
  }

  @PutMapping(value = "/pay")
  public ResponseEntity<PayResponse> pay(@RequestBody PayRequest payRequest) {
    PayResponse payWalletResponse =
        walletService.pay(payRequest.getWalletId(), payRequest.getAmount());
    return ResponseEntity.status(HttpStatus.OK).body(payWalletResponse);
  }
}
