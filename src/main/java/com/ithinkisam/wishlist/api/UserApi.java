package com.ithinkisam.wishlist.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ithinkisam.wishlist.api.exception.ErrorCode;
import com.ithinkisam.wishlist.api.exception.PayloadException;
import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.repository.UserRepository;

@RestController
@RequestMapping("/api/users")
public class UserApi {

	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/{id}")
	public User getById(@PathVariable("id") Integer id) {
		return userRepository.findById(id).orElseThrow(() ->
			new PayloadException(ErrorCode.USER.notFound(), id, HttpStatus.NOT_FOUND)
		);
	}
	
}
