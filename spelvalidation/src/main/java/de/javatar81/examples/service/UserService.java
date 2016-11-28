package de.javatar81.examples.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import de.javatar81.examples.annotations.ValidDateRange;
import de.javatar81.examples.annotations.ValidateParametersExpression;
import de.javatar81.examples.domain.User;

@Validated
@Service
public class UserService {

	@Transactional
	public User registerUser(@NotNull @Valid User newUser) {
		// Implement register logic here
		return new User();
	}

	@ValidateParametersExpression("#arg0.isBefore(#arg1)")
	@ValidDateRange
	public Collection<User> findUserByDateOfBirth(LocalDateTime from, LocalDateTime until) {
		// Implement findUser logic here
		return Collections.emptyList();
	}
}