package com.paymybuddy.paymybuddy;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Launch PayMyBuddy Application.
 *
 * @author Laura Habdul
 */
@SpringBootApplication
@EnableEncryptableProperties
public class PayMyBuddyApplication {

	/**
	 * Starts PayMyBuddy application.
	 *
	 * @param args no argument
	 */
	public static void main(final String[] args) {
		SpringApplication.run(PayMyBuddyApplication.class, args);
	}
}
