package com.CarStoreRestApi.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.CarStoreRestApi.model.User;

@Service
public class AuthService {
	
	@Autowired
	private UserService userService;
	
	//Login user (without JWT,just returning message)
	public String loginUser(String nameOrEmail,String password)
	{
		
		User user = userService.findBynameOrEmail(nameOrEmail);
		
		if(user==null)
		{
			throw new IllegalArgumentException("User not found");
		}
		
		//Check if the password matches (plain text comparsion for simplicity)
		if(!user.getPassword().equals(password))
		{
			throw new IllegalArgumentException("Invalid Password");
		}
		
		//If login successful,return success message (or manage session if needed)
		return "Login Successful!";
	}

	public boolean validatePassword(String inputPassword, String storedPassword) {
	    // Implement your password matching logic (e.g., plain or hashed comparison)
	    return inputPassword.equals(storedPassword);
	}

}


