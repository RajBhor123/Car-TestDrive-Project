package com.CarStoreRestApi.controller;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.model.Payment;
import com.CarStoreRestApi.model.TestDriveBooking;
import com.CarStoreRestApi.model.User;
import com.CarStoreRestApi.repository.PaymentRepository;
import com.CarStoreRestApi.repository.TestDriveBookingRepository;
import com.CarStoreRestApi.service.CarService;
import com.CarStoreRestApi.service.DealerService;
import com.CarStoreRestApi.service.TestDriveService;
import com.CarStoreRestApi.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class TestDriveController {

    @Autowired
    private DealerService dealerService;

    @Autowired
    private TestDriveService testDriveService;
    
    @Autowired
    private TestDriveBookingRepository bookingRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private CarService carService;
    
    @Autowired
    private UserService userService;

//    @GetMapping("/test-drive-booking/{dealerId}")
//    public String showTestDriveBookingForm(@PathVariable Long dealerId, Model model) {
//        // Fetch dealer details
//        Optional<Dealer> dealerOptional = dealerService.getDealerById(dealerId);
//        if (dealerOptional.isEmpty()) {
//            return "redirect:/dealers/location-select"; // Redirect if dealer is not found
//        }
//
//        // Add dealer info to the model
//        model.addAttribute("dealer", dealerOptional.get());
//
//        // Return view to select a test drive date
//        return "testdrivebookingform";
//    }
    
    @GetMapping("/test-drive-booking/{dealerId}")
    public String confirmBooking(@PathVariable Long dealerId, Model model) {
        // Fetch dealer details using the dealerId
        Dealer dealer = dealerService.findById(dealerId);
        model.addAttribute("dealer", dealer);
        

        // Add any other necessary attributes (e.g., booking details)
        return "testdrivebookingform";  // Return the appropriate view
    }
    
    @GetMapping("/test-drive-booking/{dealerId}/{carId}")
    public String showTestDriveBookingForm(@PathVariable Long dealerId, 
                                           @PathVariable Long carId, 
                                           Model model, HttpSession session) throws Exception {
        // Fetch car details
        Car car = carService.findById(carId);
        if (car == null) {
            throw new Exception("Car not found for ID: " + carId);
        }

        // Fetch dealer details
        Dealer dealer = dealerService.getDealerById(dealerId)
                                      .orElseThrow(() -> new Exception("Dealer not found for ID: " + dealerId));

        // Fetch the logged-in user
        User user = userService.getCurrentUser();
     // Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        
        // Add email to model (default to "Guest" if not logged in)
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        if (user == null) {
            throw new Exception("No logged-in user found.");
        }

        // Add necessary details to the model
        model.addAttribute("car", car);
        model.addAttribute("dealer", dealer);
        model.addAttribute("user", user);

        return "testdrivebookingform"; // Thymeleaf template name
    }



//    @PostMapping("/test-drive-booking/confirm")
//    public String confirmTestDriveBooking(@RequestParam("dealerId") Long dealerId, 
//                                           @RequestParam("testDriveDate") LocalDate testDriveDate,
//                                           @RequestParam("carId") Long carId,
//                                           @RequestParam("userId") Long userId,
//                                           Model model, HttpSession session) {
//        // Fetch dealer details
//        Optional<Dealer> dealerOptional = dealerService.getDealerById(dealerId);
//        if (dealerOptional.isEmpty()) {
//            return "redirect:/dealers/location-select"; // Redirect if dealer is not found
//        }
//
//        // Fetch car details
//        Car car = carService.getCarById(carId);
//
//        // Fetch user details
//        User user = userService.getUserById(userId);
//        if (user == null) {
//            return "redirect:/error"; // Handle missing user gracefully
//        }
//
//        // Retrieve user email from the session or user entity
//        String userEmail = user.getEmail(); // Assuming `User` entity has a `getEmail()` method.
//
//        // Create and save the test drive booking
//        TestDriveBooking testDriveBooking = new TestDriveBooking();
//        testDriveBooking.setDealer(dealerOptional.get());
//        testDriveBooking.setTestDriveDate(testDriveDate);
//        testDriveBooking.setCar(car);
//        testDriveBooking.setUser(user);
//        testDriveBooking.setUserEmail(userEmail); // Set the userEmail
//
//        testDriveService.saveTestDriveBooking(testDriveBooking);
//
//        // Add booking details to the model
//        model.addAttribute("booking", testDriveBooking);
//
//        // Return a confirmation page
//        return "PaymentPage";
//    }
    @PostMapping("/test-drive-booking/confirm")
    public String confirmTestDriveBooking(
            @RequestParam("dealerId") Long dealerId,
            @RequestParam("testDriveDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate testDriveDate,
            @RequestParam("carId") Long carId,
            Model model,
            HttpSession session) {
        
        // Fetch dealer details
        Optional<Dealer> dealerOptional = dealerService.getDealerById(dealerId);
        if (dealerOptional.isEmpty()) {
            return "redirect:/dealers/location-select"; // Redirect if dealer is not found
        }

        // Fetch car details
        Car car = carService.getCarById(carId);
        if (car == null) {
            return "redirect:/error"; // Handle missing car gracefully
        }

        // Retrieve logged-in user's email from the session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        System.out.println("Logged-in user email from session: " + loggedInUserEmail);
        if (loggedInUserEmail == null || loggedInUserEmail.isEmpty()) {
            return "redirect:/login"; // Redirect to login if no user is logged in
        }
        
        

        // Fetch the logged-in user from the database using the email
        User user = userService.findBynameOrEmail(loggedInUserEmail);
        if (user == null) {
        	System.out.println("User not found for email: " + loggedInUserEmail);
            return "redirect:/error"; // Handle missing user gracefully
        }

        // Create and save the test drive booking
        TestDriveBooking testDriveBooking = new TestDriveBooking();
        testDriveBooking.setDealer(dealerOptional.get());
        testDriveBooking.setTestDriveDate(testDriveDate);
        testDriveBooking.setCar(car);
        testDriveBooking.setUser(user); // Set the logged-in user
        testDriveBooking.setUserEmail(loggedInUserEmail); // Explicitly set the email
        
     // **Set the status explicitly**
      //  testDriveBooking.setStatus("CONFIRMED");

        testDriveService.saveTestDriveBooking(testDriveBooking);

        // Add booking details to the model
        model.addAttribute("booking", testDriveBooking);
        
     // Pass `bookingId` to the frontend
        model.addAttribute("bookingId", testDriveBooking.getId());


        // Return the confirmation page
        return "PaymentPage";
    }
    
    @PostMapping("/payment/success")
    public ResponseEntity<String> handlePaymentSuccess(@RequestParam Long bookingId) {
        testDriveService.updateBookingStatusAfterPayment(bookingId);
        return ResponseEntity.ok("Payment successful, booking confirmed.");
    }


//    @PostMapping("/book")
//    public ResponseEntity<?> bookTestDrive(
//            @RequestParam Long carId,
//            @RequestParam Long dealerId,
//            @RequestParam Long userId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        
//        // Validate Car
//        Car car = carService.getCarById(carId); // No changes here
//
//        // Validate Dealer
//        Dealer dealer = dealerService.getDealerById(dealerId).orElseThrow(() -> new RuntimeException("Dealer not found"));
//
//        // Validate User and retrieve email
//        User user = userService.getUserById(userId); // Assuming `userId` exists
//        if (user == null) {
//            throw new RuntimeException("User not found");
//        }
//        String userEmail = user.getEmail(); // Get email from the User object
//
//        // Create and save booking
//        TestDriveBooking booking = testDriveService.createBooking(car, dealer, user, date);
//
//        // Set the user email in the booking entity
//        booking.setUserEmail(userEmail);
//
//        // Save the booking (if necessary)
//        testDriveService.saveTestDriveBooking(booking);
//
//        return ResponseEntity.ok(booking); // Return the booking with userEmail set
//    }


    
//    @PostMapping("/book")
//    public ResponseEntity<?> bookTestDrive(
//            @RequestParam Long carId,
//            @RequestParam Long dealerId,
//            @RequestParam Long userId,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
//        // Validate Car
//        Car car = carService.getCarById(carId);//.orElseThrow(() -> new RuntimeException("Car not found"));
//        
//        // Validate Dealer
//        Dealer dealer = dealerService.getDealerById(dealerId).orElseThrow(() -> new RuntimeException("Dealer not found"));
//        
//        // Validate User
//        User user = userService.getUserById(userId);//.orElseThrow(() -> new RuntimeException("User not found"));
//
//        // Create and save booking
//        TestDriveBooking booking = testDriveService.createBooking(car, dealer, user, date);
//
//        return ResponseEntity.ok(booking);
//    }


    	
//    public TestDriveBooking createBooking(Car car, Dealer dealer, User user, LocalDate date) {
//        TestDriveBooking booking = new TestDriveBooking();
//        booking.setCar(car);
//        booking.setDealer(dealer);
//        booking.setUser(user);
//        booking.setTestDriveDate(date);
//        // Save the booking (if needed)
//        return bookingRepository.save(booking);
//    }


    
//    @GetMapping("/test-drive/confirmation")
//    public String showConfirmationPage(@RequestParam Long bookingId, Model model) {
//        TestDriveBooking booking = bookingRepository.findById(bookingId)
//                .orElseThrow(() -> new RuntimeException("Booking not found"));
//
//        if (booking.getUser() == null) {
//            model.addAttribute("error", "No user associated with the booking.");
//            return "errorPage";
//        }
//
//        Payment payment = paymentRepository.findByBookingId(bookingId)
//                .orElseThrow(() -> new RuntimeException("Payment not found"));
//
//        model.addAttribute("car", booking.getCar());
//        model.addAttribute("testDrive", booking);
//        model.addAttribute("payment", payment);
//
//        return "confirmationPage";
//    }


}