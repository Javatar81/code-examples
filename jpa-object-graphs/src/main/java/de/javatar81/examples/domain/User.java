package de.javatar81.examples.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.NamedSubgraph;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@NamedEntityGraphs({ @NamedEntityGraph(name = "user.cars", attributeNodes = @NamedAttributeNode("cars") ),
		@NamedEntityGraph(name = "user.cars.manufacturers", attributeNodes = {
				@NamedAttributeNode(value = "cars", subgraph = "cars.manufacturers") }, subgraphs = {
						@NamedSubgraph(name = "cars.manufacturers", attributeNodes = {
								@NamedAttributeNode(value = "manufacturer") }) }) })
@Entity
@Table(name = "usr")
public class User {

	@Id
	private Long id;

	@OneToMany(fetch = FetchType.LAZY)
	private Set<Car> cars;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<UserLogins> logins;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Set<Car> getCars() {
		return cars;
	}

	public void setCars(Set<Car> cars) {
		this.cars = cars;
	}

	public Set<UserLogins> getLogins() {
		return logins;
	}

	public void setLogins(Set<UserLogins> logins) {
		this.logins = logins;
	}

}
