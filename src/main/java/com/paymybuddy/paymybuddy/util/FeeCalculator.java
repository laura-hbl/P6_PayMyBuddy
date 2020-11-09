package com.paymybuddy.paymybuddy.util;

import com.paymybuddy.paymybuddy.constants.Fee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Calculates the fee of an transaction.
 *
 * @author Laura Habdul
 */
@Component
public class FeeCalculator {

    /**
     * FeeCalculator logger.
     */
    private static final Logger LOGGER = LogManager.getLogger(FeeCalculator.class);

    /**
     * Calculates fee based on amount and rate.
     *
     * @param amount of the transaction
     * @return the fee after calculation
     */
    public BigDecimal getFee(final BigDecimal amount) {
        LOGGER.debug("Inside FeeCalculator.getFee with amount : " + amount);

        BigDecimal fee = amount.multiply(Fee.FEE_RATE);

        return fee;
    }
}