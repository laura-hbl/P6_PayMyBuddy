package com.paymybuddy.paymybuddy.constants;

import java.math.BigDecimal;

/**
 * Contains transaction fee rate.
 *
 * @author Laura Habdul
 */
public final class Fee {

    /**
     * Empty constructor of class Fee.
     */
    public Fee() {
    }

    /**
     * Transaction fee rate.
     */
    public static final BigDecimal FEE_RATE = new BigDecimal("0.005");
}
