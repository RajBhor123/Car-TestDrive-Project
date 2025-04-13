package com.CarStoreRestApi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserDto {
	
	@NotBlank(message ="Username is required")
	@Size(min =3, max=20,message="Username must be between 3 and 20 characters")
	private String username;

	@Email(message="Email should be valid ")
	@NotBlank(message="Email is required")
	private String email;
	
	@NotBlank(message="Password is required")
	@Size(min =6, message="Password must be at least 6 characters")
	private String password;

	public UserDto(
			@NotBlank(message = "Username is required") @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters") String username,
			@Email(message = "Email should be valid ") @NotBlank(message = "Email is required") String email,
			@NotBlank(message = "Password is required") @Size(min = 6, message = "Password must be at least 6 characters") String password) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
	}

	public UserDto() {
		// TODO Auto-generated constructor stub
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	

}
