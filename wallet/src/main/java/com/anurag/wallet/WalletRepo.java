package com.anurag.wallet;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepo extends JpaRepository<Wallet, Integer> {
}
