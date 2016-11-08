package de.javatar81.examples;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import de.javatar81.examples.domain.User;
import de.javatar81.examples.service.UserService;

@SpringBootApplication
public class MainConfig {
	
	public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(MainConfig.class, args);
        UserService userService = applicationContext.getBean(UserService.class);
        User jdoe = getTestUser(1L, "jdoe", "John", "Doe", getDayOfBirth(1971, 12, 12), Optional.empty());
        User psmith = getTestUser(2L, "psmith", "Peter", "Smith", getDayOfBirth(1986, 1, 16), Optional.of(jdoe));
		userService.create(jdoe);
        userService.create(psmith);
        userService.create(getTestUser(3L, "pkingsley", "Pat", "Kingsley", getDayOfBirth(1965, 8, 25), Optional.of(psmith)));
        userService.create(getTestUser(4L, "lpeters", "Larry", "Peters", getDayOfBirth(1951, 7, 8), Optional.of(psmith)));
    }
	
	private static Date getDayOfBirth(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
	    cal.set(year, month, day);
	    return cal.getTime();
	}
	
	private static User getTestUser(Long id, String login, String firstName, String lastName, Date dayOfBirth, Optional<User> manager) {
		User user = new User();
		user.setId(id);
		user.setLogin(login);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDayOfBirth(dayOfBirth);
		manager.ifPresent(m -> user.setManager(m));
		return user;
	}
}
