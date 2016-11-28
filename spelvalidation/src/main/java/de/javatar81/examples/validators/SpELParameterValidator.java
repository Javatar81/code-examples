package de.javatar81.examples.validators;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import de.javatar81.examples.annotations.ValidateParametersExpression;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class SpELParameterValidator implements ConstraintValidator<ValidateParametersExpression, Object[]> {

	private ExpressionParser parser = new SpelExpressionParser();
	private Expression parsedExpression;

	public void initialize(ValidateParametersExpression constraintAnnotation) {
		parsedExpression = parser.parseExpression(constraintAnnotation.value());
	}

	public boolean isValid(Object[] values, ConstraintValidatorContext context) {
		StandardEvaluationContext spelContext = new StandardEvaluationContext(values);
		Map<String, Object> spelVars = IntStream.range(0, values.length).boxed()
				.collect(Collectors.toMap(i -> "arg" + i, i -> values[i]));
		spelContext.setVariables(spelVars);
		return (Boolean) parsedExpression.getValue(spelContext);
	}

}
