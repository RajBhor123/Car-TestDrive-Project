package com.CarStoreRestApi.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.model.PurchaseCar;
import com.CarStoreRestApi.model.TestDriveBooking;
import com.CarStoreRestApi.model.User;
import com.CarStoreRestApi.service.CarService;
import com.CarStoreRestApi.service.DealerService;
import com.CarStoreRestApi.service.PurchaseCarService;
import com.CarStoreRestApi.service.TestDriveService;
import com.CarStoreRestApi.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class DealerDashboardController {
	
	@Autowired
    private DealerService dealerService;
    
    @Autowired
    private CarService carService;
    
    @Autowired
    private TestDriveService testDriveBookingService;
    
    @Autowired
    private PurchaseCarService purchaseCarService;
	
    @Autowired
    private UserService userservice;
//	@GetMapping("/{dealerId}/cars")
//    public String showDealerCars(@PathVariable Long dealerId,HttpSession session, Model model) {
//        // Retrieve the logged-in dealer from the session
//    	 Dealer dealer = dealerService.findById(dealerId);
//        Dealer loggedInDealer = (Dealer) session.getAttribute("loggedInDealer");
//
//        if (loggedInDealer == null) {
//            // Redirect to login if no dealer is logged in
//            return "redirect:/dealers/login";
//        }
//
//        // Add the dealer to the model
//        model.addAttribute("dealer", loggedInDealer);
//        model.addAttribute("dealer", dealer);
//        
//
//        // Fetch cars for the logged-in dealer
//        List<Car> cars = carService.getCarsByDealer(loggedInDealer.getId());
//        model.addAttribute("cars", cars);
//        System.out.println("Dealer ID: " + dealerId);
//        System.out.println("Cars for Dealer ID " + dealerId + ": " + cars);
//        cars.forEach(car -> System.out.println(car.getName()));
//
//
//        return "dealer-car-listings"; // Thymeleaf template for car listings
//    }

    @GetMapping("/{dealerId}/cars")
    public String showDealerCars(@PathVariable Long dealerId, HttpSession session, Model model) {
        // Retrieve the logged-in dealer from the session
        Dealer loggedInDealer = (Dealer) session.getAttribute("loggedInDealer");

        if (loggedInDealer == null) {
            // Redirect to login if no dealer is logged in
            return "redirect:/dealers/login";
        }

        // Fetch the dealer from the database using the dealerId
        Dealer dealer = dealerService.findById(dealerId);

        if (dealer == null) {
            // Handle case if the dealer is not found
            return "error"; // Redirect to an error page
        }

        // Add the dealer to the model
        model.addAttribute("dealer", dealer);

        // Fetch all cars from the database
        Iterable<Car> cars = carService.getAllCars(); // Fetches all cars instead of filtering by dealer
        model.addAttribute("cars", cars);

        // Print debugging info (optional)
        System.out.println("Dealer ID: " + dealerId);
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
    
    @PostMapping("/cars/add")
    public String saveCar(
            @RequestParam("name") String name,
            @RequestParam("type") String type,
            @RequestParam("price") Double price,
            @RequestParam("mileage") String mileage,
            @RequestParam("engineSpecs") String engineSpecs,
            @RequestParam("quantity") String quantity,
            @RequestParam("image") MultipartFile image,
            HttpSession session) {

        // Retrieve the logged-in dealer from the session
        Dealer dealer = (Dealer) session.getAttribute("loggedInDealer");
        System.out.println("Logged-in Dealer: " + dealer);
        

        if (dealer == null) {
        	System.out.println("Dealer not found in session. Redirecting to login.");
            return "redirect:/dealers/login"; // Redirect to login if dealer is not logged in
        }

        try {
            // Define the upload directory inside the static/images folder
            String uploadDir = "C:/Users/bhorr/Downloads/CarStoreRestApi/CarStoreRestApi/src/main/resources/static/images/";
            String imageFileName = image.getOriginalFilename();
            Path imagePath = Paths.get(uploadDir + imageFileName);
            
            // Ensure the directory exists
            Files.createDirectories(imagePath.getParent());
            
            // Save the uploaded image to the specified directory
            image.transferTo(imagePath.toFile());

            // Create a new Car object and set its details
            Car car = new Car();
            car.setName(name);
            car.setType(type);
            car.setPrice(price);
            car.setMileage(mileage);
            car.setEngineSpecs(engineSpecs);
            car.setQuantity(quantity);
            System.out.println("Car Name: " + name);
            System.out.println("Car Type: " + type);

            
            // Set the relative image path for use in the front-end
            car.setImagePath(imageFileName);
            System.out.println("File name: " + image.getOriginalFilename());
            car.setDealer(dealer);

            // Save the car to the database
            carService.saveCar(car);

        } catch (IOException e) {
            e.printStackTrace();
            return "error"; // Redirect to an error page in case of issues
        }

     // After adding the car, redirect to the success page
        return "redirect:/dealer/" + dealer.getId() + "/car-added-success";
    }


    
//    @GetMapping("/add-car")
//    public String addCarPage(Model model) {
//        model.addAttribute("car", new Car());
//        return "add-car"; // Thymeleaf template for adding a new car
//    }

    @GetMapping("/{dealerId}/add-car")
    public String addCarPage(@PathVariable Long dealerId, Model model, HttpSession session) {
        // Retrieve the logged-in dealer from the session
        Dealer loggedInDealer = (Dealer) session.getAttribute("loggedInDealer");

        if (loggedInDealer == null || !loggedInDealer.getId().equals(dealerId)) {
            // If the dealer is not logged in or the session doesn't match the dealerId, redirect to login
            return "redirect:/dealers/login";
        }

        // Add a new empty Car object to the model
        model.addAttribute("car", new Car());

        // Return the view for adding a new car
        return "add-car"; // Thymeleaf template for adding a new car
    }
    
    @GetMapping("/dealer/{dealerId}/cars")
    public String getCarsForDealer(@PathVariable("dealerId") Long dealerId, Model model) {
        Dealer dealer = dealerService.findById(dealerId);

        if (dealer == null) {
            return "error"; // Handle case where dealer is not found
        }

        List<Car> cars = carService.getCarsByDealer(dealerId);
        model.addAttribute("dealer", dealer);
        model.addAttribute("cars", cars);

        return "dealer-car-listings"; // Display car listings page
    }

    @GetMapping("/dealer/{dealerId}/car-added-success")
    public String showCarAddedSuccessPage(@PathVariable("dealerId") Long dealerId, Model model) {
        Dealer dealer = dealerService.findById(dealerId);

        if (dealer == null) {
            return "error"; // Handle case where dealer is not found
        }

        model.addAttribute("dealer", dealer);
        return "car-added-success"; // Return the view for car added success page
    }
    
    @GetMapping("/{dealerId}/test-drive-bookings")
    public String viewTestDriveBookings(@PathVariable Long dealerId, Model model, HttpSession session) {
        // Retrieve the logged-in dealer from the session
        Dealer loggedInDealer = (Dealer) session.getAttribute("loggedInDealer");

        if (loggedInDealer == null || !loggedInDealer.getId().equals(dealerId)) {
            return "redirect:/dealers/login"; // Redirect to login if dealer is not logged in
        }

        // Fetch bookings for the dealer
        List<TestDriveBooking> bookings = testDriveBookingService.getBookingsByDealerId(dealerId);

        // Add bookings to the model
        model.addAttribute("dealer", loggedInDealer);
        model.addAttribute("bookings", bookings);

        return "testdrive-bookings"; // Return the view
    }
    
 // Display the "Add & Update Customer" page
    @GetMapping("/manage-customers")
    public String showCustomerPage(Model model) {
        List<User> user = userservice.getAllUser();
        model.addAttribute("customers", user);
        return "AddCustomer"; // Refers to the Thymeleaf template "add-update-customer.html"
    }

    // Handle Form Submission
    @PostMapping("/add")
    public String savePurchaseCar(PurchaseCar purchaseCar, Model model) {
        purchaseCarService.savePurchaseCar(purchaseCar);
        model.addAttribute("message", "Car purchase successfully added!");
        return "success-page"; // Name of the Thymeleaf HTML file for success
    }
}
