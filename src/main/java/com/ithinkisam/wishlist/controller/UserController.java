package com.ithinkisam.wishlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

	@GetMapping("/profile")
	public String userProfile() {
		return "secured/user-profile";
	}
	
	@GetMapping("/settings")
	public String userSettings() {
		return "secured/user-settings";
	}
	
}
