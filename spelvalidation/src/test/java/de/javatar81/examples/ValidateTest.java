package de.javatar81.examples;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.javatar81.examples.config.SpringConfig;
import de.javatar81.examples.domain.Car;
import de.javatar81.examples.domain.User;
import de.javatar81.examples.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfig.class })
public class ValidateTest {

	@Autowired
	private Validator validator;

	@Autowired
	private UserService userService;

	private User createTestUser() {
		User user = new User();
		user.setEmail("myemail@example.com");
		user.setLogin("Testuser");
		user.getCars().add(new Car());
		Calendar eighteenYearsAgo = Calendar.getInstance();
		eighteenYearsAgo.add(Calendar.YEAR, -18);
		user.setBirthday(eighteenYearsAgo.getTime());
		return user;
	}

	@Test
	public void testValidateAge() {
		// Valid email
		User userValid = createTestUser();
		Set<ConstraintViolation<User>> violations = validator.validate(userValid, new Class[] {});
		assertEquals(0, violations.size());
		// Invalid age
		User userInvalid = createTestUser();
		Calendar eighteenYearsAgo = Calendar.getInstance();
		eighteenYearsAgo.add(Calendar.YEAR, -12);
		userInvalid.setBirthday(eighteenYearsAgo.getTime());
		violations = validator.validate(userInvalid, new Class[] {});
		assertEquals(1, violations.size());
		assertEquals("birthday", violations.iterator().next().getPropertyPath().iterator().next().getName());
	}

	@Test
	public void testValidateEmail() {
		// Valid email
		User userValid = createTestUser();
		Set<ConstraintViolation<User>> violations = validator.validate(userValid, new Class[] {});
		assertEquals(0, violations.size());
		// Invalid email
		User userInvalid = createTestUser();
		userInvalid.setEmail("@example.com");
		violations = validator.validate(userInvalid, new Class[] {});
		assertEquals(1, violations.size());
		assertEquals("email", violations.iterator().next().getPropertyPath().iterator().next().getName());
	}

	@Test
	public void testValidateUsername() {
		// Valid login and email not null
		User userValid = createTestUser();
		Set<ConstraintViolation<User>> violations = validator.validate(userValid, new Class[] {});
		assertEquals(0, violations.size());
		// Valid email not null
		userValid = createTestUser();
		userValid.setLogin(null);
		violations = validator.validate(userValid, new Class[] {});
		assertEquals(0, violations.size());
		// Valid login not null
		userValid = createTestUser();
		userValid.setEmail(null);
		violations = validator.validate(userValid, new Class[] {});
		assertEquals(0, violations.size());
		// Invalid login & email null
		User userInvalid = createTestUser();
		userInvalid.setEmail(null);
		userInvalid.setLogin(null);
		violations = validator.validate(userInvalid, new Class[] {});
		// @ValidUsername and @ValidateClassExpression fail
		assertEquals(2, violations.size());
	}

	@Test
	public void testValidateCars() {
		// Valid cars.size() == 1
		User userValid = createTestUser();
		Set<ConstraintViolation<User>> violations = validator.validate(userValid, new Class[] {});
		assertEquals(0, violations.size());
		// Invalid cars.size() == 0
		User userInvalid = createTestUser();
		userInvalid.getCars().clear();
		violations = validator.validate(userInvalid, new Class[] {});
		assertEquals(1, violations.size());
	}

	@Test
	public void testValidateRegisterUser() {
		// Call with valid user
		User userValid = createTestUser();
		userService.registerUser(userValid);
		// Call with null
		try {
			userService.registerUser(null);
			fail("ConstraintViolationException expected");
		} catch (ConstraintViolationException e) {
		}
		// Call with invalid user
		User userInvalid = createTestUser();
		userInvalid.setEmail(null);
		userInvalid.setLogin(null);
		try {
			userService.registerUser(userInvalid);
			fail("ConstraintViolationException expected");
		} catch (ConstraintViolationException e) {
		}

	}

	@Test
	public void testValidateFindUser() {
		// Valid date range
		userService.findUser(LocalDateTime.now().minusHours(5), LocalDateTime.now());
		// Invalid date range
		try {
			userService.findUser(LocalDateTime.now().plusHours(5), LocalDateTime.now());
			fail("ConstraintViolationException expected");
		} catch (ConstraintViolationException e) {
			// @ValidateParametersExpression and @ValidDates fail
			assertEquals(2, e.getConstraintViolations().size());
		}
	}

}
