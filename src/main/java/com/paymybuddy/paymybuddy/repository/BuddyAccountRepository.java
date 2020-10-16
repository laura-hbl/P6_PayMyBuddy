package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.BuddyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuddyAccountRepository extends JpaRepository<BuddyAccount, Long> {
}
