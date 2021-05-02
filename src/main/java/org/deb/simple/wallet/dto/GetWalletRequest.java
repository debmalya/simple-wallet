package org.deb.simple.wallet.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class GetWalletRequest {

  @NonNull private UUID walletId;
}
