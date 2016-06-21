package de.javatar81.examples.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Car {

	@Id
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private CarManufacturer manufacturer;

	private String licensePlate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLicensePlate() {
		return licensePlate;
	}

	public void setLicensePlate(String licensePlate) {
		this.licensePlate = licensePlate;
	}

	public CarManufacturer getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(CarManufacturer manufacturer) {
		this.manufacturer = manufacturer;
	}

}
