package com.ithinkisam.wishlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

	@GetMapping("/account")
	public String userProfile() {
		return "secured/user-account";
	}
	
}
