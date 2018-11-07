package com.ithinkisam.wishlist.security;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ithinkisam.wishlist.model.Role;
import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.model.UserRegistration;
import com.ithinkisam.wishlist.repository.RoleRepository;
import com.ithinkisam.wishlist.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public boolean registerUser(UserRegistration userRegistration) {
		User newUser = new User();
		newUser.setActive(false);
		newUser.setEmail(userRegistration.getEmail());
		newUser.setFirstName(userRegistration.getFirstName());
		newUser.setLastName(userRegistration.getLastName());
		newUser.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
		
		Role userRole = roleRepository.findByRole("USER");
		newUser.setRoles(new HashSet<Role>());
		newUser.getRoles().add(userRole);
		userRepository.save(newUser);
		return true;
	}

}
