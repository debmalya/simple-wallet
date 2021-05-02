package org.deb.simple.wallet.service;

import java.util.Map;

public interface CoinsService {

  /**
   * Count coin frequency.
   * @param coins an array of integer denoting denomination of coins.
   * @return a map with denomination as key and number of occurrences as value.
   */
  Map<Integer,Long> countCoins(Integer[] coins);
}
