package com.CarStoreRestApi.dto;

import jakarta.validation.constraints.NotBlank;

public class LoginDto {
	
	@NotBlank(message="Username or email is required")
	private String usernameorEmail;
	
	@NotBlank(message="Password is required")
	private String password;

	public String getUsernameorEmail() {
		return usernameorEmail;
	}

	public void setUsernameorEmail(String usernameorEmail) {
		this.usernameorEmail = usernameorEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
}
