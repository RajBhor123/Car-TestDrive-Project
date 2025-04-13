package com.CarStoreRestApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.CarStoreRestApi.model.Payment;
import com.CarStoreRestApi.model.TestDriveBooking;
import com.CarStoreRestApi.repository.TestDriveBookingRepository;
import com.CarStoreRestApi.repository.PaymentRepository;
import com.CarStoreRestApi.service.EmailService;
import com.CarStoreRestApi.service.PaymentService;

import jakarta.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private TestDriveBookingRepository bookingRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private EmailService emailService;

    /**
     * Creates a Razorpay order before processing the payment.
     */
    @PostMapping("/create-order")
    public ResponseEntity<String> createOrder() {
        try {
            String orderResponse = paymentService.createRazorpayOrder(100.0);
            return ResponseEntity.ok(orderResponse);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating order: " + e.getMessage());
        }
    }

    /**
     * Processes the payment after Razorpay success.
     */
    @PostMapping("/process")
    public ResponseEntity<String> processPayment(@RequestBody Map<String, Object> requestData, HttpSession session) {
        try {
            Long bookingId = Long.parseLong(requestData.get("bookingId").toString());
            String paymentId = requestData.get("paymentId").toString();
            String orderId = requestData.get("orderId").toString();
            String modeOfPayment = requestData.get("modeOfPayment").toString(); // Extract mode of payment

            System.out.println("Processing payment for bookingId: " + bookingId + ", Mode: " + modeOfPayment);

            Payment payment = paymentService.processPayment(bookingId, paymentId, orderId, 100.0, modeOfPayment);

            TestDriveBooking booking = bookingRepository.findById(bookingId)
                    .orElseThrow(() -> new RuntimeException("Booking not found"));

            booking.setPayment(payment);
            bookingRepository.save(booking);

            String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
            String emailToSend = (loggedInUserEmail != null) ? loggedInUserEmail :
                                (booking.getUser() != null ? booking.getUser().getEmail() : "No email available");

            System.out.println("Sending confirmation email to: " + emailToSend);

            emailService.sendConfirmationEmail(emailToSend, booking, payment);

            return ResponseEntity.ok("Payment processed successfully!");

        } catch (Exception e) {
            System.out.println("Error processing payment: " + e.getMessage());
            return ResponseEntity.status(500).body("Payment failed: " + e.getMessage());
        }
    }

    /**
     * Loads the payment confirmation page.
     */
    @GetMapping("/confirmation")
    public String confirmationPage(@RequestParam Long bookingId, Model model) {
    	//try {
        TestDriveBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        Payment payment = booking.getPayment();
//      if (payment == null) {
//      model.addAttribute("error", "Payment details not found for this booking.");
//      return "errorPage";
//  }


        model.addAttribute("car", booking.getCar());
        model.addAttribute("testDrive", booking);
        model.addAttribute("payment", payment);

        return "confirmationPage";
//      } catch (Exception e) {
//      model.addAttribute("error", "Could not load confirmation page: " + e.getMessage());
//      return "errorPage";
//  }
    }
}

    