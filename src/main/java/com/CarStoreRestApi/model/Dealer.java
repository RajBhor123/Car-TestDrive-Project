package com.CarStoreRestApi.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;

@Entity
public class Dealer {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is mandatory")
    @Size(min = 2, max = 20, message = "Name must be between 2 and 20 characters")
    private String name;

    @NotBlank(message = "Address is mandatory")
    @Size(max = 100, message = "Address must not exceed 100 characters")
    private String address;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phoneNumber;

    @NotBlank(message = "City is mandatory")
    @Size(max = 15, message = "City must not exceed 15 characters")
    private String city;

    @NotBlank(message = "State is mandatory")
    @Size(max = 20, message = "State must not exceed 20 characters")
    private String State;

    @NotBlank(message = "Country is mandatory")
    @Size(max = 20, message = "Country must not exceed 20 characters")
    private String Country;

    @NotNull(message = "Pincode is mandatory")

    private Integer pincode;

    @NotBlank(message = "Password is mandatory")
    @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
    private String password; // New field for authentication

    @OneToMany(mappedBy = "dealer")
    private List<Car> cars; // One dealer can have multiple cars

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<Car> getCars() {
		return cars;
	}

	public void setCars(List<Car> cars) {
		this.cars = cars;
	}

	public String getcity() {
		return city;
	}

	public void setcity(String city) {
		this.city = city;
	}

	public String getState() {
		return State;
	}

	public void setState(String state) {
		State = state;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public Integer getPincode() {
		return pincode;
	}

	public void setPincode(Integer pincode) {
		this.pincode = pincode;
	}
	
	public Dealer(Long id) {
	    this.id = id;
	}
	
	 // Default constructor (required by JPA/Hibernate)
    public Dealer() {
    }

//    @Pattern(regexp = "^[a-zA-Z\\s,.'-]+$", message = "Address must contain only letters and valid characters (spaces, commas, periods, etc.)")
//    private String address;
}
