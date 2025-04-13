package com.CarStoreRestApi.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.CarStoreRestApi.model.Admin;
import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.service.AdminService;
import com.CarStoreRestApi.service.CarService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    
    @Autowired
    private CarService carService;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin-register";
    }

    @PostMapping("/register")
    public String registerAdmin(@ModelAttribute Admin admin, Model model) {
        adminService.registerAdmin(admin);
        model.addAttribute("message", "Registration successful. Please login.");
        return "redirect:/admin/login";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("admin", new Admin());
        return "admin-login";
    }

    @PostMapping("/login")
    public String loginAdmin(@ModelAttribute Admin admin, Model model) {
        Admin authenticatedAdmin = adminService.authenticateAdmin(admin.getEmail(), admin.getPassword());
        if (authenticatedAdmin != null) {
            return "redirect:/admin/dashboard";
        }
        model.addAttribute("error", "Invalid email or password.");
        return "admin-login";
    }

    @GetMapping("/dashboard")
    public String showDashboard() {
        return "admin-dashboard";
    }
    
    @GetMapping("/addcar")
    public String showAddCarForm(Model model) {
        model.addAttribute("car", new Car()); // Assuming a Car model exists
        return "addCar"; // Thymeleaf template name
    }
    
 // Handle the submission of the form to save the car
    @PostMapping("/car/add")
    public String addCar(@RequestParam("name") String name,
                         @RequestParam("type") String type,
                         @RequestParam("price") Double price,
                         @RequestParam("mileage") String mileage,
                         @RequestParam("engineSpecs") String engineSpecs,
                         @RequestParam("quantity") String quantity,
                         @RequestParam("image") MultipartFile image,
                         @RequestParam("dealerId") Long dealerId, HttpSession session) {

        // Retrieve the logged-in dealer from the session
        Dealer dealer = (Dealer) session.getAttribute("loggedInDealer");
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
            car.setImagePath(imageFileName); // Set relative image path for front-end
            car.setDealer(dealer);

            // Save the car to the database
            carService.saveCar(car);

        } catch (IOException e) {
            e.printStackTrace();
            return "error"; // Redirect to an error page in case of issues
        }

        // Redirect to a success page
        return "redirect:/admin/car-added-success";
    }
}