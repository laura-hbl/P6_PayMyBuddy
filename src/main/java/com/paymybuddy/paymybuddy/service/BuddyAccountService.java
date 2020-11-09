package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.model.BuddyAccount;
import com.paymybuddy.paymybuddy.repository.BuddyAccountRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Contains methods that allow interaction between BuddyAccount business logic and BuddyAccount repository.
 *
 * @author Laura Habdul
 * @see IBuddyAccountService
 */
@Service
public class BuddyAccountService implements IBuddyAccountService {

    /**
     * BuddyAccountService logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(BuddyAccountService.class);

    /**
     * BuddyAccountRepository instance.
     */
    private final BuddyAccountRepository buddyAccountRepository;

    /**
     * Constructor of class BuddyAccountService.
     * Initialize buddyAccountRepository.
     *
     * @param buddyAccountRepository BuddyAccountRepository instance
     */
    @Autowired
    public BuddyAccountService(final BuddyAccountRepository buddyAccountRepository) {
        this.buddyAccountRepository = buddyAccountRepository;
    }

    /**
     * Saves BuddyAccount by calling buddyAccountRepository's save method.
     *
     * @param buddyAccount the buddyAccount to be saved
     * @return The buddyAccount saved
     */
    public BuddyAccount saveBuddyAccount(final BuddyAccount buddyAccount) {
        LOGGER.debug("Inside BuddyAccountService.saveBuddyAccount");
        return buddyAccountRepository.save(buddyAccount);
    }
}
