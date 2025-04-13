package com.CarStoreRestApi.dto;

import com.CarStoreRestApi.model.Dealer;

public class DealerDto {

    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private String city;  // Correct name for Thymeleaf
    private String state;
    private String country;
    private Integer pincode;

    // Constructor to map from Dealer entity
    public DealerDto(Dealer dealer) {
        this.id = dealer.getId();
        this.name = dealer.getName();
        this.address = dealer.getAddress();
        this.phoneNumber = dealer.getPhoneNumber();
        this.city = dealer.getcity();  // Map the entity's field to DTO's property
        this.state = dealer.getState();
        this.country = dealer.getCountry();
        this.pincode = dealer.getPincode();
    }

    

	// Getters and setters follow JavaBean conventions
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

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getPincode() {
        return pincode;
    }

    public void setPincode(Integer pincode) {
        this.pincode = pincode;
    }
}
