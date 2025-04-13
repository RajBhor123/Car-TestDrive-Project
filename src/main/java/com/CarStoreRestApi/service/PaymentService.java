package com.CarStoreRestApi.service;

import java.time.LocalDateTime;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.CarStoreRestApi.model.Payment;
import com.CarStoreRestApi.model.TestDriveBooking;
import com.CarStoreRestApi.repository.PaymentRepository;
import com.CarStoreRestApi.repository.TestDriveBookingRepository;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;

@Service
public class PaymentService {

    private static final String RAZORPAY_KEY_ID = "rzp_test_M2tFx24BuEeDZe"; // Replace with actual Key ID
    private static final String RAZORPAY_SECRET = "l2Whuc1nZOcVLSM8Yrtvx5ic"; // Replace with actual Secret Key

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TestDriveBookingRepository bookingRepository;

    /**
     * Creates a Razorpay order for the given amount.
     */
    public String createRazorpayOrder(Double amount) {
        try {
            RazorpayClient razorpay = new RazorpayClient(RAZORPAY_KEY_ID, RAZORPAY_SECRET);

            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount * 100); // Convert to paise
            orderRequest.put("currency", "INR");
            orderRequest.put("payment_capture", 1);

            Order order = razorpay.orders.create(orderRequest);
            return order.toString();
        } catch (Exception e) {
            throw new RuntimeException("Error creating Razorpay order: " + e.getMessage());
        }
    }

    /**
     * Processes the payment after Razorpay success.
     */
    public Payment processPayment(Long bookingId, String paymentId, String orderId, Double amount, String modeOfPayment) {
        System.out.println("Processing payment for bookingId: " + bookingId + ", Mode: " + modeOfPayment);

        TestDriveBooking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found"));

        // Check if payment already exists for the booking
        if (paymentRepository.findByBooking(booking) != null) {
            throw new RuntimeException("Payment already exists for this booking.");
        }

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(amount);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setModeOfPayment(modeOfPayment); // ✅ Save the payment mode
        payment.setStatus("SUCCESSFUL");
        payment.setPaymentId(paymentId);
        payment.setOrderId(orderId);

        // Save payment
        Payment savedPayment = paymentRepository.save(payment);
        System.out.println("Payment saved with ID: " + savedPayment.getId());

        return savedPayment;
    }
}
