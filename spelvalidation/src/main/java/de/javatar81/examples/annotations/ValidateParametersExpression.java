package de.javatar81.examples.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import de.javatar81.examples.validators.SpELParameterValidator;

@Constraint(validatedBy = SpELParameterValidator.class)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidateParametersExpression {

	String message() default "{parameters.validation.message}";

	Class<?>[]groups() default {};

	Class<? extends Payload>[]payload() default {};

	String value();
}
