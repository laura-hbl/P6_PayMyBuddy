package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository interface that provides methods that permit interaction with database bank_account table.
 *
 * @author Laura Habdul
 */
@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {
}
