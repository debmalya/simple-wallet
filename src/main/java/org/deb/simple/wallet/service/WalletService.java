package org.deb.simple.wallet.service;

import java.util.UUID;
import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.deb.simple.wallet.dto.GetWalletResponse;
import org.deb.simple.wallet.dto.PayResponse;

public interface WalletService {

  /**
   * Create a wallet with passed coins.
   *
   * @param coinList list of coin denominations
   * @return status of wallet creation. If successful returns newly created wallet id. Otherwise
   *     returns list of errors encountered.
   */
  CreateWalletResponse createWallet(Integer[] coinList);

  /**
   * Get coin details of wallet.
   *
   * @param walletId to be retrieved.
   * @return Wallet Response with message containing coin details.
   */
  GetWalletResponse getWallet(UUID walletId);

  /**
   * Try to pay the amount from existing coins of wallet.
   *
   * @param walletId from which wallet payment will be made.
   * @param amount amount to be paid.
   * @return PayResponse indicating the status of transaction and after transaction exiting coin
   *     information.
   */
  PayResponse pay(UUID walletId, int amount);
}
