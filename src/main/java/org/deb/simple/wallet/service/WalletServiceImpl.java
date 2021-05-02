package org.deb.simple.wallet.service;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.deb.simple.wallet.dto.WalletError;
import org.deb.simple.wallet.entity.Coins;
import org.deb.simple.wallet.entity.Wallet;
import org.deb.simple.wallet.repository.CoinsRepository;
import org.deb.simple.wallet.repository.WalletRepository;
import org.deb.simple.wallet.util.MessageUtil;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WalletServiceImpl implements WalletService {
  private final CoinsRepository coinsRepository;
  private final WalletRepository walletRepository;
  private final CoinsServiceImpl coinsService;

  @Override
  @Transactional(TxType.REQUIRES_NEW)
  public CreateWalletResponse createWallet(Integer[] coinList) {
    var createWalletResponse = new CreateWalletResponse();
    List<WalletError> walletErrorList = new ArrayList<>();
    var wallet = new Wallet();
    var coinFrequencyMap = coinsService.countCoins(coinList);
    List<Coins> coinsList = new ArrayList<>();
    coinFrequencyMap.forEach((denomination, quantity) -> {
      if (denomination < 1){
        addWalletError(walletErrorList, denomination);
      }else {
        var coins = new Coins();
        coins.setDenomination(denomination);
        coins.setQuantity(quantity);
        coinsList.add(coins);
      }
    });
    wallet.setCoinsList(coinsRepository.saveAll(coinsList));
    wallet = walletRepository.save(wallet);

    createWalletResponse.setWalletId(wallet.getWalletId());
    createWalletResponse.setMessage(MessageUtil.createSuccessMessage());
    createWalletResponse.setWalletErrorList(walletErrorList);

    return createWalletResponse;
  }

  private void addWalletError(List<WalletError> walletErrorList, Integer coin) {
    var walletError = new WalletError();
    String errorMessage = MessageUtil.negativeCoinValue(coin);
    log.error(errorMessage);
    walletError.setErrorMessage(errorMessage);
    walletErrorList.add(walletError);
  }
}
