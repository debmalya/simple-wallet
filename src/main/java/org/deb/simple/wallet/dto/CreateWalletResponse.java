package org.deb.simple.wallet.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CreateWalletResponse {
  private UUID walletId;
  private String message;

  @JsonProperty(value="errors")
  private List<WalletError> walletErrorList;
}
