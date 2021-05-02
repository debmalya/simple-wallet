package org.deb.simple.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WalletError {
  @JsonProperty(value = "errorMessage")
  private String errorMessage;

  @JsonProperty(value = "isRecoverable")
  private boolean isRecoverable;
}
