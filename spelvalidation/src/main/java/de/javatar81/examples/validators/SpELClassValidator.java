package de.javatar81.examples.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import de.javatar81.examples.annotations.ValidateClassExpression;

public class SpELClassValidator implements ConstraintValidator<ValidateClassExpression, Object> {

	private ValidateClassExpression annotation;
	private ExpressionParser parser = new SpelExpressionParser();

	public void initialize(ValidateClassExpression constraintAnnotation) {
		annotation = constraintAnnotation;
		parser.parseExpression(constraintAnnotation.value());
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		StandardEvaluationContext spelContext = new StandardEvaluationContext(value);
		return (Boolean) parser.parseExpression(annotation.value()).getValue(spelContext);
	}

}
