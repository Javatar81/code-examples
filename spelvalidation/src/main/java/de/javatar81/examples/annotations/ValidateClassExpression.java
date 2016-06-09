package de.javatar81.examples.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

import de.javatar81.examples.validators.SpELClassValidator;

@Target({ ElementType.TYPE, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { SpELClassValidator.class })
@Documented
@Repeatable(ValidateClassExpressions.class)
public @interface ValidateClassExpression {

	String message() default "{expression.validation.message}";

	Class<?>[]groups() default {};

	Class<? extends Payload>[]payload() default {};

	String value();

}
