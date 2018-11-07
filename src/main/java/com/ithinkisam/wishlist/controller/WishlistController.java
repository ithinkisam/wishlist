package com.ithinkisam.wishlist.controller;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.model.Wish;
import com.ithinkisam.wishlist.model.support.Range;
import com.ithinkisam.wishlist.model.support.Reference;
import com.ithinkisam.wishlist.repository.UserRepository;
import com.ithinkisam.wishlist.repository.WishRepository;

@Controller
public class WishlistController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WishRepository wishRepository;
	
	@GetMapping("/wishlist")
	public String wishlist(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		return "secured/wishlist";
	}
	
	@PostMapping("/wish")
	public String addWish(String[] urls, String description, String price) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Wish wish = new Wish();
		wish.setDescription(description);
		wish.setPrice(new Range(new BigDecimal(price), new BigDecimal(price)));
		wish.setUser(user);
		
		if (urls != null && urls.length > 0) {
			wish.setReferences(new ArrayList<>());
			for (String url : urls) {
				Reference reference = new Reference();
				try {
					reference.setUrl(new URL(url));
					wish.getReferences().add(reference);
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		wishRepository.save(wish);
		
		return "redirect:/wishlist";
	}
	
}
