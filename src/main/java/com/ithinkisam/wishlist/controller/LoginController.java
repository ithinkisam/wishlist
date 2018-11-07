package com.ithinkisam.wishlist.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.model.UserRegistration;
import com.ithinkisam.wishlist.security.UserService;

@Controller
public class LoginController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/login")
	public String login() {
		return "login";
	}
	
	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}
	
	@PostMapping("/registration")
	public String createNewUser(Model model, @Valid UserRegistration userRegistration, BindingResult bindingResult) {
		if (!userRegistration.getPassword().equals(userRegistration.getMatchingPassword())) {
			bindingResult.rejectValue("password", "registration.password.mismatch", "Passwords do not match.");
			return "registration";
		}
		
		User existingUser = userService.findUserByEmail(userRegistration.getEmail());
		if (existingUser != null) {
			bindingResult.rejectValue("email", "registration.email.exists", "There is already an account associated with that email address.");
		}
		
		if (!bindingResult.hasErrors() && userService.registerUser(userRegistration)) {
			model.addAttribute("m", "registration.success");
			return "redirect:/login";
		}
		return "registration";
	}
	
	@GetMapping("/forgot-password")
	public String forgotPassword() {
		return "forgot.password";
	}
	
}
