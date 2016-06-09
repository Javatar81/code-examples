package de.javatar81.examples.validators;

import java.util.Calendar;
import java.util.Date;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.javatar81.examples.annotations.ValidAge;

public class AgeValidator implements ConstraintValidator<ValidAge, Date> {

	private ValidAge constraintAnnotation;

	public void initialize(ValidAge constraintAnnotation) {
		this.constraintAnnotation = constraintAnnotation;
	}

	public boolean isValid(Date value, ConstraintValidatorContext context) {
		if (value == null) {
			return true;
		} else {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR, -constraintAnnotation.min());
			return !cal.getTime().before(value);
		}
	}

}