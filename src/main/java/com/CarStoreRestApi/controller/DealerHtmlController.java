package com.CarStoreRestApi.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CarStoreRestApi.dto.DealerDto;
import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.repository.DealerRepository;
import com.CarStoreRestApi.service.CarService;
import com.CarStoreRestApi.service.DealerService;
import com.CarStoreRestApi.service.EmailService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/dealers")
public class DealerHtmlController {

    @Autowired
    private DealerService dealerService;
    
    @Autowired
    private CarService carService;
    
    @Autowired
    private DealerRepository dealerRepository;
    
    @Autowired
    private EmailService emailService;
    

    // Endpoint to display the location selection page
    @GetMapping("/location-select")
    public String showLocationSelection(@RequestParam(value = "carId", required = false) Long carId, Model model, HttpSession session) {
        List<String> cities = dealerService.getDealerCities();
        model.addAttribute("cities", cities);
        
     // Store the carId in session or pass it to the model
        session.setAttribute("carId", carId);

        // Optionally, retrieve the car details to show on this page
        Car car = carService.getCarById(carId);
        model.addAttribute("car", car);
        // Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        
        if (carId == null) {
            System.out.println("carId is null, please ensure it is passed correctly in the request.");
        } else {
            System.out.println("Car ID received: " + carId);
        }
        // Pass the carId to the model if available
        //model.addAttribute("carId", carId);
        System.out.println("Car ID received: " + carId);
        return "location-select"; // Your HTML file name
    }
    
    
    @GetMapping("/show")
    public String showDealersByCity(@RequestParam("city") String city, 
                                    @RequestParam(value = "carId", required = false) Long carId, 
                                    Model model, HttpSession session) {
        if (carId == null) {
            carId = (Long) session.getAttribute("carId");  // Fetch from session if not provided in URL
        }

        if (carId == null) {
            model.addAttribute("error", "Car ID is missing.");
            return "error-page"; // Show an error page if carId is still null
        }

        Car car = carService.getCarById(carId);
        if (car == null) {
            model.addAttribute("error", "Car not found!");
            return "error-page"; // Show an error page if the car is not found
        }

        List<Dealer> dealersInCity = dealerService.getDealersByCity(city);
        model.addAttribute("dealers", dealersInCity);
        model.addAttribute("city", city);
        model.addAttribute("car", car);  // Add the car object to the model

        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");

        return "dealers-list"; // Return the view that will display the dealers
    }
    
    @GetMapping("/register")
    public String showRegistrationForm() {
        return "dealer-registration";
    }

    @PostMapping("/register")
    public String registerDealer(@RequestParam Map<String, String> params, Model model) {
        Dealer dealer = new Dealer();
        dealer.setName(params.get("name"));
        dealer.setAddress(params.get("address"));
        dealer.setPhoneNumber(params.get("phoneNumber"));
        dealer.setcity(params.get("city"));
        dealer.setState(params.get("state"));
        dealer.setCountry(params.get("country"));
        dealer.setPincode(Integer.parseInt(params.get("pincode")));
        dealer.setPassword(params.get("password"));

        dealerService.saveDealer(dealer);

        return "redirect:/dealers/login";
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "dealer-login";
    }
    
 

//    @PostMapping("/login")
//    public String login(@RequestParam String name, 
//                        @RequestParam String password, 
//                        HttpSession session) {
//        try {
//            // Fetch dealer by name
//            Dealer dealer = dealerService.findByName(name);  // Ensure the service handles this logic
//            
//            if (dealer == null) {
//                return "redirect:/dealers/login?error=true";  // Dealer not found
//            }
//
//            System.out.println("Fetched Dealer: " + dealer);
//            System.out.println(dealer.getPassword());
//            System.out.println(password);
//
//            // Direct password validation without authService
//            if (dealer.getPassword().equals(password)) {  // Password comparison
//                // Store dealer object in session (can be used in the dashboard)
//                session.setAttribute("loggedInDealer", dealer);
//                System.out.println("Dealer logged in successfully: " + dealer.getName());
//
//                return "redirect:/dealers/dashboard";  // Redirect to dealer dashboard
//            } else {
//                return "redirect:/dealers/login?error=true";  // Incorrect password
//            }
//        } catch (Exception e) {
//            return "redirect:/dealers/login?error=true";  // Redirect on error
//        }
//    }
//
//
//
//   
//    @GetMapping("/dashboard")
//    public String showDealerDashboard(@PathVariable Long dealerId,HttpSession session, Model model) {
//        // Retrieve the logged-in dealer from the session
//    	// Retrieve the logged-in dealer from the session
//   	 	Dealer dealer = dealerService.findById(dealerId);
//        Dealer loggedInDealer = (Dealer) session.getAttribute("loggedInDealer");
//        
//        if (loggedInDealer == null) {
//            // If the dealer is not logged in, redirect to the login page
//            return "redirect:/dealers/login";
//        }
//
//        // Convert logged-in dealer to DealerDto
//        DealerDto dealerDto = new DealerDto(loggedInDealer);
//
//        // Add the DTO to the model to be accessed in the Thymeleaf template
//        model.addAttribute("dealer", dealerDto);
//        model.addAttribute("dealer", dealer);
//        
//        // Return the dashboard page for the dealer
//        return "dealer-dashboard";
//    }
    
    
 // In the login method, update the redirect to include the dealerId
    @PostMapping("/login")
    public String login(@RequestParam String name, 
                        @RequestParam String password, 
                        HttpSession session) {
        try {
            // Fetch dealer by name
            Dealer dealer = dealerService.findByName(name);  
            
            if (dealer == null) {
                return "redirect:/dealers/login?error=true";  // Dealer not found
            }

            System.out.println("Fetched Dealer: " + dealer);
            System.out.println(dealer.getPassword());
            System.out.println(password);

            // Direct password validation without authService
            if (dealer.getPassword().equals(password)) {  // Password comparison
                // Store dealer object in session (can be used in the dashboard)
                session.setAttribute("loggedInDealer", dealer);
                System.out.println("Dealer logged in successfully: " + dealer.getName());

                // Redirect to dashboard with dealerId as a path variable
                return "redirect:/dealers/dashboard/" + dealer.getId();  // Pass the dealerId in the URL
            } else {
                return "redirect:/dealers/login?error=true";  // Incorrect password
            }
        } catch (Exception e) {
            return "redirect:/dealers/login?error=true";  // Redirect on error
        }
    }

    @GetMapping("/dashboard/{dealerId}")
    public String showDealerDashboard(@PathVariable Long dealerId, HttpSession session, Model model) {
        // Retrieve the logged-in dealer from the session
        Dealer loggedInDealer = (Dealer) session.getAttribute("loggedInDealer");

        // Ensure the loggedInDealer in session matches the dealerId in the URL
        if (loggedInDealer == null || !loggedInDealer.getId().equals(dealerId)) {
            // If the dealer is not logged in or the IDs don't match, redirect to the login page
            return "redirect:/dealers/login";
        }

        // Convert logged-in dealer to DealerDto
        DealerDto dealerDto = new DealerDto(loggedInDealer);

        // Add the DTO to the model to be accessed in the Thymeleaf template
        model.addAttribute("dealer", dealerDto);
        
        // Return the dashboard page for the dealer
        return "dealer-dashboard";
    }

    @GetMapping("/{dealerId}/cars")
    public String showDealerCars(@PathVariable Long dealerId,HttpSession session, Model model) {
        // Retrieve the logged-in dealer from the session
    	 Dealer dealer = dealerService.findById(dealerId);
        Dealer loggedInDealer = (Dealer) session.getAttribute("loggedInDealer");

        if (loggedInDealer == null) {
            // Redirect to login if no dealer is logged in
            return "redirect:/dealers/login";
        }

        // Add the dealer to the model
        model.addAttribute("dealer", loggedInDealer);
        model.addAttribute("dealer", dealer);
        

        // Fetch cars for the logged-in dealer
        List<Car> cars = carService.getCarsByDealer(loggedInDealer.getId());
        model.addAttribute("cars", cars);
        System.out.println("Dealer ID: " + dealerId);
        System.out.println("Cars for Dealer ID " + dealerId + ": " + cars);
        cars.forEach(car -> System.out.println(car.getName()));


        return "dealer-car-listings"; // Thymeleaf template for car listings
    }

    @GetMapping("/{dealerId}/profile")
    public String viewDealerProfile(@PathVariable("dealerId") Long dealerId, Model model) {
        Optional<Dealer> optionalDealer = dealerService.getDealerById(dealerId); // Get the Optional<Dealer>
        
        if (optionalDealer.isPresent()) {
            model.addAttribute("dealer", optionalDealer.get()); // Unwrap the dealer and add to the model
        } else {
            // Handle case where dealer is not found (optionalDealer.isEmpty())
            return "error"; // Redirect to an error page or handle as needed
        }
        
        return "dealer-profile"; // Return the dealer profile page
    }





    
//    @GetMapping("/confirm-booking")
//    public String confirmBooking(@RequestParam("dealerId") Long dealerId, 
//                                 @RequestParam(value = "carId", required = false) Long carId, 
//                                 Model model, HttpSession session) {
//        if (carId == null) {
//            model.addAttribute("error", "Car ID is missing.");
//            return "error-page"; // Redirect to an error page or show a specific message
//        }
//
//        Optional<Dealer> dealer = dealerService.getDealerById(dealerId);
//        if (!dealer.isPresent()) {
//            model.addAttribute("error", "Dealer not found!");
//            return "error-page"; // Show dealer not found error
//        }
//        Car car = carService.getCarById(carId);
//        if (car == null) {
//            model.addAttribute("error", "Car not found!");
//            return "error-page"; // Show car not found error
//        }
//
//        model.addAttribute("dealer", dealer.get());
//        model.addAttribute("car", car);
//
//        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
//        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
//
//        return "confirm-booking"; // Your HTML file name
//    }
//    
//    @PostMapping("/process-payment")
//    public String processPayment(@RequestParam Map<String, String> requestParams, Model model, HttpSession session) {
//        String date = requestParams.get("date");
//        Long dealerId = Long.parseLong(requestParams.get("dealerId"));
//        Long carId = Long.parseLong(requestParams.get("carId"));
//
//        // Fetch the dealer and handle the Optional
//        Optional<Dealer> optionalDealer = dealerService.getDealerById(dealerId);
//        if (!optionalDealer.isPresent()) {
//            model.addAttribute("error", "Dealer not found!");
//            return "error-page"; // Replace with an appropriate error page
//        }
//        Dealer dealer = optionalDealer.get();
//
//        // Fetch the car
//        Car car = carService.getCarById(carId);
//
//        String loggedInUserEmail = (String) session.
//
//        // Send Email Confirmation
//        emailService.sendEmail(
//            loggedInUserEmail,
//            "Test Drive Confirmation",
//            "Your test drive for " + car.getName() + " is confirmed with " + dealer.getName() +
//            " on " + date + ".\n\n" +
//            "Car Details:\n" +
//            "Name: " + car.getName() + "\n" +
//            "Price: ₹" + car.getPrice() + "\n" +
//            "Mileage: " + car.getMileage() + " km/l\n\n" +
//            "Dealer Details:\n" +
//            "Name: " + dealer.getName() + "\n" +
//            "Address: " + dealer.getAddress() + "\n" +
//            "Phone: " + dealer.getPhoneNumber() + "\n\n" +
//            "Thank you for choosing our service!"
//        );
//
//        // Add details to the model
//        model.addAttribute("dealer", dealer);
//        model.addAttribute("car", car);
//        model.addAttribute("email", loggedInUserEmail);
//        model.addAttribute("date", date);
//
//        return "confirmation-page"; // HTML for the confirmation page
//    }

}


