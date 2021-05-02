package org.deb.simple.wallet.util;

import java.util.Arrays;
import java.util.UUID;

public class MessageUtil {

  private MessageUtil() {}

  private static final String ERR_NEGATIVE_VALUE = "Coin value ('%d') should be greater than zero.";

  public static String createErrorMessage(String messageFormat, int value) {
    return String.format(messageFormat, value);
  }

  public static String createErrorMessage(String messageFormat, UUID value) {
    return String.format(messageFormat, value.toString());
  }

  public static String negativeCoinValue(int negativeCoinValue) {
    return createErrorMessage(ERR_NEGATIVE_VALUE, negativeCoinValue);
  }

  public static String createSuccessMessage() {
    return "Success";
  }

  public static String paymentSuccessfull(int amount) {
    return String.format("Successfully paid %d", amount);
  }

  public static String paymentFailed(int amount) {
    return String.format("You do not have sufficient coins to pay %d", amount);
  }

  public static String setCurrentMessage(Integer[] coins) {
    return String.format("current coins are %s", Arrays.toString(coins));
  }
}
