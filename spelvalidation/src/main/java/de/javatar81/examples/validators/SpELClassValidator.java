package de.javatar81.examples.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import de.javatar81.examples.annotations.ValidateClassExpression;

public class SpELClassValidator implements ConstraintValidator<ValidateClassExpression, Object> {

	
	private ExpressionParser parser = new SpelExpressionParser();
	private Expression parsedExpression;

	public void initialize(ValidateClassExpression constraintAnnotation) {
		parsedExpression = parser.parseExpression(constraintAnnotation.value());
	}

	public boolean isValid(Object value, ConstraintValidatorContext context) {
		StandardEvaluationContext spelContext = new StandardEvaluationContext(value);
		return (Boolean) parsedExpression.getValue(spelContext);
	}

}
