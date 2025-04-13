package com.CarStoreRestApi.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.model.TestDriveBooking;
import com.CarStoreRestApi.model.User;
import com.CarStoreRestApi.repository.DealerRepository;
import com.CarStoreRestApi.repository.TestDriveBookingRepository;

@Service
public class TestDriveService {

	 @Autowired
	    private TestDriveBookingRepository testDriveBookingRepository;

	    public TestDriveBooking saveTestDriveBooking(TestDriveBooking booking) {
	        return testDriveBookingRepository.save(booking);
	    }

	    public TestDriveBooking getBookingById(Long id) {
	        return testDriveBookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
	    }
	    
	    public TestDriveBooking createBooking(Car car, Dealer dealer, User user, LocalDate date) {
	        TestDriveBooking booking = new TestDriveBooking();
	        booking.setCar(car);
	        booking.setDealer(dealer);
	        booking.setUser(user);
	        booking.setTestDriveDate(date);
	        booking.setStatus("PENDING");
	        return testDriveBookingRepository.save(booking);
	    }

	    public void confirmBooking(Long bookingId) {
	        TestDriveBooking booking = testDriveBookingRepository.findById(bookingId)
	                .orElseThrow(() -> new RuntimeException("Booking not found"));
	        booking.setStatus("CONFIRMED");
	        testDriveBookingRepository.save(booking);
	    }
	    

	    public List<TestDriveBooking> getBookingsByDealerId(Long dealerId) {
	        return testDriveBookingRepository.findByDealerId(dealerId);
	    }
	    
	    public void updateBookingStatusAfterPayment(Long bookingId) {
	        TestDriveBooking booking = testDriveBookingRepository.findById(bookingId)
	                .orElseThrow(() -> new RuntimeException("Booking not found"));

	        if (booking.getPayment() != null) { // Ensure payment exists
	            booking.setStatus("CONFIRMED");
	            testDriveBookingRepository.save(booking);
	        }
	    }

}
