package com.CarStoreRestApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.CarStoreRestApi.model.Payment;
import com.CarStoreRestApi.model.TestDriveBooking;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender mailSender;

//    public void sendConfirmationEmail(String to, TestDriveBooking booking, Payment payment) {
//        String subject = "Test Drive Booking Confirmed!";
//        String message = String.format("""
//            Dear User,
//
//            Your test drive for the car %s has been successfully booked.
//
//            Test Drive Date: %s
//            Dealer: %s
//
//            Car Details:
//            - Name: %s
//            - Type: %s
//            - Price: Rs. %.2f
//            - Mileage: %s
//            - Engine Specs: %s
//            - Quantity: %s
//
//            Payment Details:
//            - Status: %s
//            - Amount: Rs. %.2f
//            - Payment Mode: %s
//
//            Thank you!
//            """,
//            booking.getCar().getName(),
//            booking.getTestDriveDate(),
//            booking.getDealer().getName(),
//            booking.getCar().getName(),
//            booking.getCar().getType(),
//            booking.getCar().getPrice(),
//            booking.getCar().getMileage(),
//            booking.getCar().getEngineSpecs(),
//            booking.getCar().getQuantity(),
//            payment.getStatus(),
//            payment.getAmount(),
//            payment.getModeOfPayment()
//        );
//
//        SimpleMailMessage email = new SimpleMailMessage();
//        email.setTo(to);
//        email.setSubject(subject);
//        email.setText(message);
//        mailSender.send(email);
//    }
    
	public void sendConfirmationEmail(String to, TestDriveBooking booking, Payment payment) {
	    try {
	        String subject = "Test Drive Booking Confirmed!";
	        String message = String.format("""
	              Dear User,
	  
	              Your test drive for the car %s has been successfully booked.
	  
	              Test Drive Date: %s
	              Dealer: %s
	  
	              Car Details:
	              - Name: %s
	              - Type: %s
	              - Price: Rs. %.2f
	              - Mileage: %s
	              - Engine Specs: %s
	              - Quantity: %s
	  
	              Payment Details:
	              - Status: %s
	              - Amount: Rs. %.2f
	              - Payment Mode: %s
	  
	              Thank you!
	              """,
	              booking.getCar().getName(),
	              booking.getTestDriveDate(),
	              booking.getDealer().getName(),
	              booking.getCar().getName(),
	              booking.getCar().getType(),
	              booking.getCar().getPrice(),
	              booking.getCar().getMileage(),
	              booking.getCar().getEngineSpecs(),
	              booking.getCar().getQuantity(),
	              payment.getStatus(),
	              payment.getAmount(),
	              payment.getModeOfPayment()
	          );

	        SimpleMailMessage email = new SimpleMailMessage();
	        email.setTo(to); // Single recipient
	        email.setSubject(subject);
	        email.setText(message);

	        System.out.println("Sending email to: " + to); // Debug log
	        mailSender.send(email);
	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println("Failed to send email to: " + to);
	    }
	}





}