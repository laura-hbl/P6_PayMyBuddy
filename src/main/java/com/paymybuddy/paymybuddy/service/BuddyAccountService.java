package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.repository.BuddyAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuddyAccountService implements IBuddyAccountService {

    private BuddyAccountRepository buddyAccountRepository;

    @Autowired
    public BuddyAccountService(BuddyAccountRepository buddyAccountRepository) {
        this.buddyAccountRepository = buddyAccountRepository;
    }

    public void saveBuddyAccount(BuddyAccount buddyAccount) {
        buddyAccountRepository.save(buddyAccount);
    }
}
