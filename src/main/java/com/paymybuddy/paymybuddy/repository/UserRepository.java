package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * JPA repository interface that provides methods that permit interaction with database user table.
 *
 * @author Laura Habdul
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

     /**
      * Finds an user by the email address.
      *
      * @param email email of the user
      * @return The user found
      */
     User findByEmail(String email);
}