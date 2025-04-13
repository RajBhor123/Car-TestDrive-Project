package com.CarStoreRestApi.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.CarStoreRestApi.model.Payment;
import com.CarStoreRestApi.model.TestDriveBooking;

public interface PaymentRepository extends JpaRepository<Payment, Long>{
	
	Optional<Payment> findByBookingId(Long bookingId);

	Payment findByBooking(TestDriveBooking booking);

}
