package com.CarStoreRestApi.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Payment {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

	@Column
    private Long bookingId;

    @Column
    private Double amount;

    @Column
    private LocalDateTime paymentDate;

    @Column
    private String modeOfPayment;

    @Column
    private String status; // PENDING, SUCCESSFUL, FAILED
    
    @Column(unique = true, nullable = false)
    private String paymentId;  // ADD THIS FIELD

    @Column(unique = true, nullable = false)
    private String orderId;  // ADD THIS FIELD
    
    @OneToOne
    @JoinColumn(name = "testbooking_id", referencedColumnName = "id", unique = true)
    private TestDriveBooking booking;


    // Getters and Setters
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getBookingId() {
		return bookingId;
	}

	public void setBookingId(Long bookingId) {
		this.bookingId = bookingId;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public LocalDateTime getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDateTime paymentDate) {
		this.paymentDate = paymentDate;
	}

	public String getModeOfPayment() {
		return modeOfPayment;
	}

	public void setModeOfPayment(String modeOfPayment) {
		this.modeOfPayment = modeOfPayment;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
//	 public TestDriveBooking getTestDriveBooking() {
//	        return testDriveBooking;
//	    }
//
//	    public void setTestDriveBooking(TestDriveBooking testDriveBooking) {
//	        this.testDriveBooking = testDriveBooking;
//	    }
	
	public String getPaymentId() { // ADD THIS METHOD
        return paymentId;
    }

    public void setPaymentId(String paymentId) { // ADD THIS METHOD
        this.paymentId = paymentId;
    }

    public String getOrderId() { // ADD THIS METHOD
        return orderId;
    }

    public void setOrderId(String orderId) { // ADD THIS METHOD
        this.orderId = orderId;
    }

		public TestDriveBooking getBooking() {
			return booking;
		}

		public void setBooking(TestDriveBooking booking) {
			this.booking = booking;
		}

}
