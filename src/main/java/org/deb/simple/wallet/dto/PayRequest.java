package org.deb.simple.wallet.dto;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PayRequest {
   private UUID walletId;
   private int amount;
}
