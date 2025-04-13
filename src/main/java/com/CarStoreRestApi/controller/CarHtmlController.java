package com.CarStoreRestApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CarStoreRestApi.model.Car;
import com.CarStoreRestApi.model.PurchaseCar;
import com.CarStoreRestApi.repository.CarRepository;
import com.CarStoreRestApi.service.CarHtmlService;
import com.CarStoreRestApi.service.PurchaseCarService;

import jakarta.servlet.http.HttpSession;


@Controller
@RequestMapping("/cars")
public class CarHtmlController {

    @Autowired
    private CarHtmlService carService;
    
    @Autowired
    private CarRepository carrepoobj;

    
    // Display all cars
    @GetMapping
    public String listCars(Model model, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/login"; // Redirect to login page if not logged in
        }
        List<Car> cars = carService.getAllCars();
        model.addAttribute("cars", cars);
     // Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        
        // Add email to model (default to "Guest" if not logged in)
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        return "carList"; // Refers to carList.html
    }

    // Display car details
    @GetMapping("/{id}")
    public String viewCarDetails(@PathVariable Long id, Model model, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/login"; // Redirect to login page if not logged in
        }
        Car car = carService.getCarById(id);
        model.addAttribute("car", car);
     // Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        
        // Add email to model (default to "Guest" if not logged in)
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        return "carDetails"; // Refers to carDetails.html
    }

    // Fetch SUVs
    @GetMapping("/suvs")
    public String getSUVs(Model model, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Car> suvs = carService.getCarsByType("SUV");
        model.addAttribute("cars", suvs);
     // Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        
        // Add email to model (default to "Guest" if not logged in)
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        return "carList";
    }

    // Fetch Sedans
    @GetMapping("/sedans")
    public String getSedans(Model model, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Car> sedans = carService.getCarsByType("Sedan");
        model.addAttribute("cars", sedans);
     // Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        
        // Add email to model (default to "Guest" if not logged in)
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        return "carList";
    }

    // Fetch Hatchbacks
    @GetMapping("/hatchbacks")
    public String getHatchbacks(Model model, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Car> hatchbacks = carService.getCarsByType("Hatchback");
     // Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        
        // Add email to model (default to "Guest" if not logged in)
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        model.addAttribute("cars", hatchbacks);
        return "carList";
    }

    // Fetch Sports Cars
    @GetMapping("/sport")
    public String getSportsCars(Model model, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/login";
        }
        List<Car> sportsCars = carService.getCarsByType("Sport");
     // Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        
        // Add email to model (default to "Guest" if not logged in)
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        model.addAttribute("cars", sportsCars);
        return "carList";
    }
    
    
    
    @GetMapping("/search")
    public String carsearch(@RequestParam(required = false) String query, Model model, HttpSession session) {
        List<Car> carobj = query == null || query.isEmpty() ? carService.getAllCars() : carrepoobj.findByNameOrType(query, query);
        model.addAttribute("cars", carobj);
        model.addAttribute("username", session.getAttribute("loggedInUser"));
        return "carList";
    }
    
   
    // Utility to check if a user is logged in
    private boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("loggedInUserEmail") != null;
    }
    
    
}


