package com.CarStoreRestApi.model;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class TestDriveBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;


    @ManyToOne
    @JoinColumn(name = "dealer_id")
    private Dealer dealer;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private LocalDate testDriveDate; // Store the test drive date (e.g., "2024-12-25")
    
    @Column
    private String status; // PENDING, CONFIRMED, CANCELLED
    
    @Column
    private String userEmail;
    
    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Payment payment;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

	public Car getCar() {
		return car;
	}

	public void setCar(Car car) {
		this.car = car;
	}

	public LocalDate getTestDriveDate() {
		return testDriveDate;
	}

	public void setTestDriveDate(LocalDate testDriveDate) {
		this.testDriveDate = testDriveDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public User getUser() {
	    return user;
	}

	public void setUser(User user) {
	    this.user = user;
	}
	
	public Payment getPayment() {
        return payment;
    }

//    public void setPayment(Payment payment) {
//        this.payment = payment;
//    }
    
    public void setPayment(Payment payment) {
        this.payment = payment;
        if (payment != null) {
            this.status = "CONFIRMED";
        }
    }

}
