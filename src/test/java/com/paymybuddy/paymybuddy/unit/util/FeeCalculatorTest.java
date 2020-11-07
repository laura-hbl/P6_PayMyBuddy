package com.paymybuddy.paymybuddy.unit.util;

import com.paymybuddy.paymybuddy.constants.Fee;
import com.paymybuddy.paymybuddy.util.FeeCalculator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

public class FeeCalculatorTest {

    private static final FeeCalculator feeCalculator = new FeeCalculator();

    @Test
    @DisplayName("If amount corresponds to 1, fee should be equal to FEE_RATE")
    public void givenAnAmount_whenGetFee_thenFeeIsEqualFeeRate() {

        BigDecimal fee = feeCalculator.getFee(BigDecimal.ONE);

        assertThat(fee).isEqualTo(Fee.FEE_RATE);
    }

    @Test
    @DisplayName("If amount corresponds to 355.05, fee should be equal to 355.05 multiply FEE_RATE")
    public void givenA_whenGetFee_thenFeeIsEqualTo() {

        BigDecimal fee = feeCalculator.getFee(BigDecimal.valueOf(355.05));

        assertThat(fee).isEqualTo(BigDecimal.valueOf(355.05).multiply(Fee.FEE_RATE));
    }

    @Test
    @DisplayName("If amount corresponds to 999.99, fee should be equal to 999.99 multiply FEE_RATE")
    public void given_whenGetFee_thenFee() {

        BigDecimal fee = feeCalculator.getFee(BigDecimal.valueOf(999.99));

        assertThat(fee).isEqualTo(BigDecimal.valueOf(999.99).multiply(Fee.FEE_RATE));
    }
}
