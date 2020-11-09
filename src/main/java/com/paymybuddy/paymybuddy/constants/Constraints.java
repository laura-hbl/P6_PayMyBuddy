package com.paymybuddy.paymybuddy.constants;

/**
 * Contains the different validator constraints.
 *
 * @author Laura Habdul
 */
public final class Constraints {

    /**
     * Empty constructor of class Constraints.
     */
    public Constraints() {
    }

    /**
     * Maximum number of characters allowed for the first name.
     */
    public static final int FIRST_NAME_MAX_SIZE = 70;

    /**
     * Maximum number of characters allowed for the last name.
     */
    public static final int LAST_NAME_MAX_SIZE = 70;

    /**
     * Minimum number of characters required for email.
     */
    public static final int EMAIL_MIN_SIZE = 3;

    /**
     * Minimum number of characters required for password.
     */
    public static final int PASSWORD_MIN_SIZE = 5;

    /**
     * Maximum number of characters allowed for phone number.
     */
    public static final int PHONE_MAX_SIZE = 20;

    /**
     * Minimum number of characters required for IBAN.
     */
    public static final int IBAN_MIN_SIZE = 14;

    /**
     * Maximum number of characters allowed for IBAN.
     */
    public static final int IBAN_MAX_SIZE = 34;

    /**
     * Minimum number of characters required for BIC.
     */
    public static final int BIC_MIN_SIZE = 8;

    /**
     * Maximum number of characters allowed for BIC.
     */
    public static final int BIC_MAX_SIZE = 11;

    /**
     * Maximum number of characters allowed for description.
     */
    public static final int DESCRIPTION_MAX_SIZE = 100;
}
