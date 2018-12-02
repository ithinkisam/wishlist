package com.ithinkisam.wishlist.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ithinkisam.wishlist.api.exception.ErrorCode;
import com.ithinkisam.wishlist.api.exception.PayloadException;
import com.ithinkisam.wishlist.model.ManagedWish;
import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.repository.ManagedWishRepository;
import com.ithinkisam.wishlist.repository.UserRepository;

@RestController
@RequestMapping("/api/managed-wishes")
public class ManagedWishApi {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ManagedWishRepository managedWishRepository;
	
	@PostMapping("/{id}/purchase")
	public ManagedWish purchase(@PathVariable("id") Integer id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		// Wish must exist
		ManagedWish wish = managedWishRepository.findById(id).orElseThrow(() ->
				new PayloadException(ErrorCode.WISH.notFound(), id, HttpStatus.NOT_FOUND));
		
		// Can't purchase if it's already been purchased
		if (wish.getPurchased()) {
			throw new PayloadException(ErrorCode.WISH.invalid(), id, HttpStatus.BAD_REQUEST);
		}
		
		wish.setPurchased(true);
		wish.setPurchaser(user);
		return managedWishRepository.save(wish);
	}

	@PostMapping("/{id}/unpurchase")
	public ManagedWish unpurchase(@PathVariable("id") Integer id) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		// Wish must exist
		ManagedWish wish = managedWishRepository.findById(id).orElseThrow(() ->
				new PayloadException(ErrorCode.WISH.notFound(), id, HttpStatus.NOT_FOUND));
		
		// Can't unpurchase if it hasn't already been purchased
		if (!wish.getPurchased()) {
			throw new PayloadException(ErrorCode.WISH.invalid(), id, HttpStatus.BAD_REQUEST);
		}

		// Can't unpurchase a wish you didn't purchase
		if (wish.getPurchased() && !user.equals(wish.getPurchaser())) {
			throw new PayloadException(ErrorCode.WISH.invalid(), id, HttpStatus.BAD_REQUEST);
		}
		
		wish.setPurchased(false);
		wish.setPurchaser(null);
		return managedWishRepository.save(wish);
	}

}
