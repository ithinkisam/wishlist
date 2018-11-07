package com.ithinkisam.wishlist.model;

import lombok.Data;

@Data
public class UserRegistration {

	private String email;
	
	private String password;
	
	private String matchingPassword;
	
	private String firstName;
	
	private String lastName;
	
}
