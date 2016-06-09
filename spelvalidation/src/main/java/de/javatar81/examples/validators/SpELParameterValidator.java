package de.javatar81.examples.validators;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import de.javatar81.examples.annotations.ValidateParametersExpression;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class SpELParameterValidator implements ConstraintValidator<ValidateParametersExpression, Object[]> {

	private ValidateParametersExpression validParameters;
	private ExpressionParser parser = new SpelExpressionParser();

	public void initialize(ValidateParametersExpression constraintAnnotation) {
		this.validParameters = constraintAnnotation;
		parser.parseExpression(constraintAnnotation.value());
	}

	public boolean isValid(Object[] values, ConstraintValidatorContext context) {
		StandardEvaluationContext spelContext = new StandardEvaluationContext(values);
		Map<String, Object> spelVars = IntStream.range(0, values.length).boxed()
				.collect(Collectors.toMap(i -> "arg" + i, i -> values[i]));
		spelContext.setVariables(spelVars);
		Boolean evaluationValue = (Boolean) parser.parseExpression(validParameters.value()).getValue(spelContext);
		return evaluationValue;
	}

}
