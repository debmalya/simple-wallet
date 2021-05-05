package org.deb.simple.wallet.repository;

import org.deb.simple.wallet.entity.Cash;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CoinsRepository extends JpaRepository<Cash, Integer> {}
