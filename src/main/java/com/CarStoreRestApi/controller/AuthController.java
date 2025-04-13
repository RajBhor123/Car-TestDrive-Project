package com.CarStoreRestApi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.CarStoreRestApi.dto.LoginDto;
import com.CarStoreRestApi.dto.LoginResponse;
import com.CarStoreRestApi.service.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
	
	@Autowired
	private AuthService authService;
	
	//Login method without JWT (just return the message)
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginDto loginDto)
	{
		try {
			//Authenticate the user
			String message = authService.loginUser(loginDto.getUsernameorEmail(),loginDto.getPassword());
			return ResponseEntity.ok(new LoginResponse(message));
		}
		catch(IllegalArgumentException e) {
			//Return error message if login fails
			return ResponseEntity.badRequest().body(new LoginResponse("Login failed : "+e.getMessage()));
		}
	}


	
}
