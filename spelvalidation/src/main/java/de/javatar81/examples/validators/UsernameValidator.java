package de.javatar81.examples.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import de.javatar81.examples.annotations.ValidUsername;
import de.javatar81.examples.domain.User;

public class UsernameValidator implements ConstraintValidator<ValidUsername, User> {

	@Override
	public void initialize(ValidUsername constraintAnnotation) {

	}
	
	@Override
	public boolean isValid(User user, ConstraintValidatorContext context) {
		return !(user.getLogin() == null && user.getEmail() == null);
	}

}
