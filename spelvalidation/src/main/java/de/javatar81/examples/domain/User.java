package de.javatar81.examples.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.constraints.Email;

import de.javatar81.examples.annotations.ValidAge;
import de.javatar81.examples.annotations.ValidUsername;
import de.javatar81.examples.annotations.ValidateClassExpression;

@ValidateClassExpression(value = "!(#this.login == null && #this.email == null)", message = "Login or email must be defined.")
@ValidateClassExpression(value = "!#this.cars.isEmpty()", message = "User must have at least one car.")
@ValidUsername
public class User {

	@ValidAge(min = 13)
	private Date birthday;

	private String login;

	@Email
	private String email;

	private List<Car> cars = new ArrayList<>();

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

}
