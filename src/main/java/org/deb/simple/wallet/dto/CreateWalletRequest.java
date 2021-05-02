package org.deb.simple.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class CreateWalletRequest {
  @JsonProperty(value = "coins")
  @NonNull
  private Integer[] coins;
}
