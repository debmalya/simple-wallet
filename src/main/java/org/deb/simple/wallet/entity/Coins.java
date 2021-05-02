package org.deb.simple.wallet.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.NonNull;

@Data
@Entity
@NoArgsConstructor
public class Coins {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;

  @NonNull
  private Integer denomination;

  @NonNull
  private Long quantity;

}
