package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository interface that provides methods that permit interaction with database transaction table.
 *
 * @author Laura Habdul
 */
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
