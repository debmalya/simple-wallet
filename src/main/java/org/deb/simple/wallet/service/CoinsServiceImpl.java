package org.deb.simple.wallet.service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CoinsServiceImpl implements CoinsService {

  @Override
  public Map<Integer, Long> countCoins(Integer[] coins) {
    return Stream.of(coins)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }
}
