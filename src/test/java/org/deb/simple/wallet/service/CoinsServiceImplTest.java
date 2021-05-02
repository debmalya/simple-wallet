package org.deb.simple.wallet.service;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CoinsServiceImplTest {

  CoinsServiceImpl coinsService = new CoinsServiceImpl();

  @Test
  void countCoins() {
    Integer[] coins = new Integer[] {2, 3, 1, 2, 1};
    var coinFrequencyMap = coinsService.countCoins(coins);
    assertNotNull(coinFrequencyMap);
    assertEquals(3, coinFrequencyMap.size());
    assertEquals(2, coinFrequencyMap.get(1));
    assertEquals(2, coinFrequencyMap.get(2));
    assertEquals(1, coinFrequencyMap.get(3));
  }

  @Test
  void nullCoins() {
    try {
      var coinFrequencyMap = coinsService.countCoins(null);
    } catch (IllegalArgumentException iae) {
      assertTrue(true);
    }
  }
}
