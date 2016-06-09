package de.javatar81.examples.validators;

import java.time.LocalDateTime;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import de.javatar81.examples.annotations.ValidDates;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class DatesValidator implements ConstraintValidator<ValidDates, Object[]> {

	public void initialize(ValidDates constraintAnnotation) {

	}

	public boolean isValid(Object[] value, ConstraintValidatorContext context) {
		if (value.length != 2) {
			throw new IllegalArgumentException("Only methods with 2 arguments supported!");
		}
		if (value[0] == null || value[1] == null) {
			return true;
		}
		if (!(value[0] instanceof LocalDateTime) || !(value[1] instanceof LocalDateTime)) {
			throw new IllegalArgumentException("Parameters must be of type LocalDateTime!");
		}
		return ((LocalDateTime) value[0]).isBefore(((LocalDateTime) value[1]));
	}

}
