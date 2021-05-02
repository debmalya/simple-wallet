package org.deb.simple.wallet.repository;

import java.util.UUID;
import org.deb.simple.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, UUID> {

}
