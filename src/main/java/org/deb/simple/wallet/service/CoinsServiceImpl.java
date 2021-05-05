package org.deb.simple.wallet.service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.stereotype.Service;

@Service
public class CoinsServiceImpl implements CoinsService {

  @Override
  public Map<Integer, Long> countCoins(Integer[] cash) {
    if (cash == null) {
      throw new IllegalArgumentException("Cash value must be provided");
    }
    return Stream.of(cash)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
  }
}
