package com.ithinkisam.wishlist.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.repository.UserRepository;

@ControllerAdvice
public class AdviceController {

	@Autowired
	private UserRepository userRepository;
	
	@ModelAttribute("user")
	public User getUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return userRepository.findByEmail(auth.getName());
	}
	
}
