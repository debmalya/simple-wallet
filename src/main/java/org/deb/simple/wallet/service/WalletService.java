package org.deb.simple.wallet.service;

import org.deb.simple.wallet.dto.CreateWalletResponse;

public interface WalletService {

  /**
   * Create a wallet with passed coins.
   * @param coinList list of coin denominations
   * @return status of wallet creation. If successful returns newly created wallet id.
   * Otherwise returns list of errors encountered.
   */
  CreateWalletResponse createWallet(Integer[] coinList);
}
