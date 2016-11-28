package de.javatar81.examples.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import de.javatar81.examples.validators.UsernameValidator;

@Target({ ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { UsernameValidator.class })
@Documented
public @interface ValidUsername {
	String message() default "{username.validation.message}";

	Class<?>[]groups() default {};

	Class<? extends Payload>[] payload() default {};

}
