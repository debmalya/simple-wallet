package org.deb.simple.wallet.controller;

import lombok.RequiredArgsConstructor;
import org.deb.simple.wallet.dto.CreateWalletRequest;
import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.deb.simple.wallet.service.WalletServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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
}