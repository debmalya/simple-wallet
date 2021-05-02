package org.deb.simple.wallet.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.transaction.Transactional;
import javax.transaction.Transactional.TxType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.deb.simple.wallet.dto.CreateWalletResponse;
import org.deb.simple.wallet.dto.GetWalletResponse;
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
    coinFrequencyMap.forEach(
        (denomination, quantity) -> {
          if (denomination < 1) {
            addWalletError(walletErrorList, denomination);
          } else {
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

  @Override
  public GetWalletResponse getWallet(UUID walletId) {
    Optional<Wallet> retrievedWallet = walletRepository.findById(walletId);
    var getWalletResponse = new GetWalletResponse();
    List<WalletError> errorList = new ArrayList<>();
    List<Integer> coinList = new ArrayList<>();
    if (retrievedWallet.isPresent()) {
      setWalletResponse(retrievedWallet.get(), getWalletResponse, coinList);
    } else {
      walletNotFound(walletId, getWalletResponse, errorList);
    }

    getWalletResponse.setErrors(errorList);
    return getWalletResponse;
  }

  private void walletNotFound(
      UUID walletId, GetWalletResponse getWalletResponse, List<WalletError> errorList) {
    getWalletResponse.setWalletId(null);
    var walletError = new WalletError();
    walletError.setErrorMessage(
        MessageUtil.createErrorMessage("'%s' matching wallet not found", walletId));
    errorList.add(walletError);
  }

  private void setWalletResponse(
      Wallet wallet, GetWalletResponse getWalletResponse, List<Integer> coinList) {
    getWalletResponse.setWalletId(wallet.getWalletId());
    getWalletResponse.setMessage(walletToString(wallet.getCoinsList(), coinList));
  }

  private String walletToString(List<Coins> coinsList, List<Integer> coinList) {
    for (Coins eachCoin : coinsList) {
      var count = 0;
      while (count < eachCoin.getQuantity()) {
        coinList.add(eachCoin.getDenomination());
        count++;
      }
    }
    return MessageUtil.setCurrentMessage(coinList.toArray(new Integer[0]));
  }

  private void addWalletError(List<WalletError> walletErrorList, Integer coin) {
    var walletError = new WalletError();
    String errorMessage = MessageUtil.negativeCoinValue(coin);
    log.error(errorMessage);
    walletError.setErrorMessage(errorMessage);
    walletErrorList.add(walletError);
  }
}
