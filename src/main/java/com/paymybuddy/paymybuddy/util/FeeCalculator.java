package com.paymybuddy.paymybuddy.util;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class FeeCalculator {

    public static final BigDecimal FEE_RATE = new BigDecimal("0.005");

    public BigDecimal getFee(BigDecimal amount) {

          BigDecimal fee = amount.multiply(FEE_RATE);
            return fee;
    }
}
