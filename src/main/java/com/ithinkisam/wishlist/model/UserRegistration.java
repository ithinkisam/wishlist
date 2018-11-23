package com.ithinkisam.wishlist.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserRegistration {

	@NotBlank(message = "Email is required")
	private String email;
	
	@Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
	private String password;
	
	private String matchingPassword;
	
	@NotBlank(message = "First name is required")
	private String firstName;
	
	@NotBlank(message = "Last name is required")
	private String lastName;
	
}
