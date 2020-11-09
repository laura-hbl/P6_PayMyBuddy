package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.BuddyAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository interface that provides methods that permit interaction with database buddy_account table.
 *
 * @author Laura Habdul
 */
@Repository
public interface BuddyAccountRepository extends JpaRepository<BuddyAccount, Long> {
}
