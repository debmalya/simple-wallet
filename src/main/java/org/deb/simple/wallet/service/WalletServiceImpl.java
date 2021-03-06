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
import org.deb.simple.wallet.dto.PayResponse;
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

  @Override
  public PayResponse pay(UUID walletId, int amount) {
    Optional<Wallet> retrievedWallet = walletRepository.findById(walletId);
    List<WalletError> errorList = new ArrayList<>();
    var payResponse = new PayResponse();
    if (retrievedWallet.isPresent()) {
      deductAmount(retrievedWallet.get(), amount, payResponse);
    } else {
      // wallet not found.
      walletNotFound(walletId, payResponse, errorList);
    }
    payResponse.setErrors(errorList);
    return payResponse;
  }

  private void deductAmount(Wallet retrievedWallet, int amount, PayResponse payResponse) {
    payResponse.setWalletId(retrievedWallet.getWalletId());
    int requestedAmount = amount;
    List<Coins> unmodifiedCoins = keepOriginalCopy(retrievedWallet);

    List<Integer> modifiedCoinIndexes = new ArrayList<>();
    for (var i = 0; i < retrievedWallet.getCoinsList().size() && amount > 0; i++) {
      var eachCoin = retrievedWallet.getCoinsList().get(i);
      if (eachCoin.getQuantity() > 0) {
        amount = payCoinByCoin(amount, modifiedCoinIndexes, i, eachCoin);
      }
    }

    var message = "";
    if (amount == 0) {
      // successfully deducted the amount
      modifyWallet(retrievedWallet, modifiedCoinIndexes);
      message =
          String.format(
              "%s %s",
              MessageUtil.paymentSuccessfull(requestedAmount),
              walletToString(retrievedWallet.getCoinsList(), new ArrayList<>()));

    } else {
      message =
          String.format(
              "%s %s",
              MessageUtil.paymentFailed(requestedAmount),
              walletToString(unmodifiedCoins, new ArrayList<>()));
    }
    payResponse.setMessage(message);
  }

  @Transactional
  private void modifyWallet(Wallet retrievedWallet, List<Integer> modifiedCoinIndexes) {
    for (Integer modifiedCoinIndex : modifiedCoinIndexes) {
      coinsRepository.save(retrievedWallet.getCoinsList().get(modifiedCoinIndex));
    }
    walletRepository.save(retrievedWallet);
  }

  private int payCoinByCoin(int amount, List<Integer> modifiedCoinIndexes, int i, Coins eachCoin) {
    int deductedQuantity = amount / eachCoin.getDenomination();
    if (deductedQuantity > 0) {
      if (eachCoin.getQuantity() >= deductedQuantity) {
        amount -= eachCoin.getDenomination() * deductedQuantity;
      } else {
        amount -= eachCoin.getDenomination() * eachCoin.getQuantity();
        deductedQuantity = eachCoin.getQuantity().intValue();
      }
    } else {
      eachCoin.setDenomination(eachCoin.getDenomination() - amount);
      amount = 0;
    }

    eachCoin.setQuantity(eachCoin.getQuantity() - deductedQuantity);
    modifiedCoinIndexes.add(i);
    return amount;
  }

  private List<Coins> keepOriginalCopy(Wallet retrievedWallet) {
    List<Coins> unmodifiedCoins = new ArrayList<>();
    for (Coins eachCoin : retrievedWallet.getCoinsList()) {
      Coins coins = new Coins();
      coins.setDenomination(eachCoin.getDenomination());
      coins.setQuantity(eachCoin.getQuantity());
      unmodifiedCoins.add(coins);
    }
    return unmodifiedCoins;
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
