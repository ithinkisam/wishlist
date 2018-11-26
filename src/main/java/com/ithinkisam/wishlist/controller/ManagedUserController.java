package com.ithinkisam.wishlist.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ithinkisam.wishlist.model.ManagedUser;
import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.repository.UserRepository;

@Controller
@RequestMapping("/managed-users")
public class ManagedUserController {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping
	public String getManagedUsersPage(Model model) {
		model.addAttribute("newManagedUser", new ManagedUser());
		return "secured/managed-users";
	}
	
	@PostMapping
	public String createManagedUser(@Valid ManagedUser managedUser, BindingResult bindingResult) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		if (bindingResult.hasErrors()) {
			return "secured/managed-users";
		}
		
		user.getManagedUsers().add(managedUser);
		userRepository.save(user);
		
		return "redirect:/managed-users";
	}
	
}
