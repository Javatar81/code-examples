package de.javatar81.examples.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import de.javatar81.examples.validators.DateRangeValidator;

@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { DateRangeValidator.class })
@Documented
public @interface ValidDateRange {
	String message() default "{dates.validation.message}";

	Class<?>[]groups() default {};

	Class<? extends Payload>[]payload() default {};

}
