package com.paymybuddy.paymybuddy.util;

import com.paymybuddy.paymybuddy.constants.Fee;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FeeCalculator {

    private static final Logger LOGGER = LogManager.getLogger(FeeCalculator.class);

    public BigDecimal getFee(BigDecimal amount) {
        LOGGER.debug("Inside FeeCalculator.getFee with amount : " +amount);

        BigDecimal fee = amount.multiply(Fee.FEE_RATE);

        return fee;
    }
}