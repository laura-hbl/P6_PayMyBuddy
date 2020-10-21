package com.paymybuddy.paymybuddy.util;

import com.paymybuddy.paymybuddy.exception.BadRequestException;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class Validator {

    private static final Logger LOGGER = LoggerFactory.getLogger("Validator");

    private javax.validation.Validator validator;

    public void validate(Object objectToValidate) {

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        Set<ConstraintViolation<Object>> constraintViolations = validator.validate(objectToValidate);
        if (constraintViolations.size() > 0) {
            LOGGER.error("ERROR: a constraint was violated");
            throw new BadRequestException("Missing or incomplete information. Please fill all required fields!");
        }
    }
}
