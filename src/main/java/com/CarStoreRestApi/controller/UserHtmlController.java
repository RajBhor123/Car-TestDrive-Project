package com.CarStoreRestApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.CarStoreRestApi.dto.UserDto;
import com.CarStoreRestApi.model.PurchaseCar;
import com.CarStoreRestApi.model.User;
import com.CarStoreRestApi.service.AuthService;
import com.CarStoreRestApi.service.PurchaseCarService;
import com.CarStoreRestApi.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class UserHtmlController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private PurchaseCarService purchaseCarService;
	
	@GetMapping("/register")
	public String showRegistrationForm() {
	    return "UserRegistration"; // Points to UserRegistration.html in templates
	}

	@PostMapping("/register")
	public String registerUser(
	    @RequestParam("username") String username,
	    @RequestParam("email") String email,
	    @RequestParam("password") String password) {

	    UserDto userDto = new UserDto();
	    userDto.setUsername(username);
	    userDto.setEmail(email);
	    userDto.setPassword(password);

	    User user = userService.registerUser(userDto);
	    return "Login";
	}

    @Autowired
    private AuthService authService;

//    // Display the login page
//    @GetMapping("/login")
//    public String showLoginPage() {
//        return "Login"; // Points to login.html in the templates directory
//    }

    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Invalid username, email, or password.");
        }
        return "Login";
    }

//    // Process the login form without using Model
//    @PostMapping("/login")
//    public String login(@RequestParam String usernameOrEmail, 
//                        @RequestParam String password, 
//                        @RequestParam(required = false) String error, 
//                        HttpServletResponse response,HttpSession session) {
//        try {
//        	System.out.println(usernameOrEmail);
//        	System.out.println(password) ;
//            String message = authService.loginUser(usernameOrEmail, password);
//            System.out.println(message);
//            session.setAttribute("loggedInUser", usernameOrEmail);
//            System.out.println(session.getAttribute("loggedInUser"));
//            return "redirect:/Dashboard"; // Indicating that the response is already handled
//        } catch (Exception e) {
//            return "redirect:/Dashboard"; // Redirect to login page with error query
//        }
//    }

    // Display the login success page
    @GetMapping("/login-success")
    public String loginSuccess(@RequestParam String message, HttpServletRequest request) {
        request.setAttribute("successMessage", message);
        return "login-success"; // Points to login-success.html
    }
	
//    @PostMapping("/login")
//    public String login(@RequestParam String usernameOrEmail, 
//                        @RequestParam String password, 
//                        HttpSession session) {
//        try {
//            User user = userService.findBynameOrEmail(usernameOrEmail);
//            session.setAttribute("email", user.getEmail());
//
//            System.out.println("Fetched User: " + user);
//            System.out.println(user.getPassword());
//            System.out.println(password);
//            
//            if (authService.validatePassword(password, user.getPassword())) {
//                session.setAttribute("loggedInUserEmail", user.getEmail());
//                System.out.println("Email set in session: " + user.getEmail());
//                return "redirect:/Dashboard";
//            } else {
//                return "redirect:/login?error=true";
//            }
//        } catch (Exception e) {
//            return "redirect:/login?error=true";
//        }
//    }
//    
    @PostMapping("/login")
    public String login(@RequestParam String usernameOrEmail, 
                        @RequestParam String password, 
                        HttpSession session) {
        try {
            User user = userService.findBynameOrEmail(usernameOrEmail);
            

            
            if (user == null) {
                return "redirect:/login?error=true"; // Redirect if user is not found
            }
            session.setAttribute("email", user.getEmail());
            System.out.println("Fetched User: " + user);
            System.out.println(user.getPassword());
            System.out.println(password);

            if (authService.validatePassword(password, user.getPassword())) {
                session.setAttribute("loggedInUserEmail", user.getEmail());
                return "redirect:/Dashboard";
            } else {
                return "redirect:/login?error=true";
            }
        } catch (Exception e) {
            return "redirect:/login?error=true";
        }
    }



    @GetMapping("/Dashboard")
    public String showDashboard(HttpSession session, HttpServletRequest request) {
        String email = (String) session.getAttribute("loggedInUserEmail");
        System.out.println("Email retrieved from session: " + email);
        request.setAttribute("email", email);
        return "Dashboard";
    }

    @GetMapping("/view-purchase")
    public String viewPurchase(@RequestParam("email") String email, Model model) {
        List<PurchaseCar> purchases = purchaseCarService.getPurchaseByEmail(email);
        if (purchases.isEmpty()) {
            model.addAttribute("message", "No purchases found for the provided email.");
            return "error-page"; // A simple error page with a back-to-home button
        }
        model.addAttribute("purchases", purchases);
        return "view-purchase"; // Thymeleaf template to display purchase details
    }


}
