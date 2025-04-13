package com.CarStoreRestApi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.CarStoreRestApi.model.Dealer;
import com.CarStoreRestApi.repository.DealerRepository;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class NavigationController {
	
	@Autowired
	private DealerRepository dealerRepository;
	
	@GetMapping("/account")
    public String myAccountPage( Model model,HttpSession session) {
		
		// Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        return "myAccount"; // Returns the view named "myAccount.html"
    }

    @GetMapping("/contact")
    public String contactPage(Model model,HttpSession session) {
    	
    	// Retrieve user email from session
        String loggedInUserEmail = (String) session.getAttribute("loggedInUserEmail");
        model.addAttribute("email", loggedInUserEmail != null ? loggedInUserEmail : "Guest");
        return "contact"; // Returns the view named "contact.html"
    }
    
    @RequestMapping("/logout")
    public String logout(HttpServletRequest request) {
        // Invalidate the session
        request.getSession().invalidate();
        return "Login"; // Redirect to login page
    }

    @GetMapping("/dealers-list")
    public String showDealersList(Model model) {
        List<Dealer> dealers = dealerRepository.findAll();  // Fetch all dealers from DB
        model.addAttribute("dealers", dealers);  // Add dealers list to model
        return "DealerList";  // Thymeleaf view name
    }
    
    @GetMapping("/about")
    public String aboutPage() {
        return "About"; // Returns the "about.html" page
    }
}
