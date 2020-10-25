package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.repository.BuddyAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BuddyAccountService implements IBuddyAccountService {

    private static final Logger LOGGER = LogManager.getLogger(BuddyAccountService.class);

    private final BuddyAccountRepository buddyAccountRepository;

    @Autowired
    public BuddyAccountService(final BuddyAccountRepository buddyAccountRepository) {
        this.buddyAccountRepository = buddyAccountRepository;
    }

    public void saveBuddyAccount(BuddyAccount buddyAccount) {
        LOGGER.debug("Inside BuddyAccountService.saveBuddyAccount");
        buddyAccountRepository.save(buddyAccount);
    }
}
