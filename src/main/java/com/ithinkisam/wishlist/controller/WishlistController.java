package com.ithinkisam.wishlist.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Parser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ithinkisam.wishlist.controller.support.Message;
import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.model.Wish;
import com.ithinkisam.wishlist.model.support.Range;
import com.ithinkisam.wishlist.model.support.Reference;
import com.ithinkisam.wishlist.repository.ReferenceRepository;
import com.ithinkisam.wishlist.repository.UserRepository;
import com.ithinkisam.wishlist.repository.WishRepository;

@Controller
public class WishlistController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private WishRepository wishRepository;
	
	@Autowired
	private ReferenceRepository referenceRepository;
	
	@Autowired
	private Parser<Range> rangeParser;
	
	@GetMapping("/wishlist")
	public String wishlist(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		return "secured/wishlist";
	}
	
	@GetMapping("/wish/{id}")
	@ResponseBody
	public Wish getWish(@PathVariable("id") Integer wishId) {
		return wishRepository.findById(wishId).orElse(new Wish());
	}
	
	@PostMapping("/wish")
	public String addWish(@RequestParam(name = "urls", defaultValue = "") String[] urls,
			String description,
			@RequestParam(name = "price", required = false) String price) throws ParseException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Wish wish = new Wish();
		wish.setDescription(description);
		wish.setPrice(rangeParser.parse(price, Locale.getDefault()));
		wish.setPurchased(false);
		wish.setUser(user);
		
		if (urls != null && urls.length > 0) {
			wish.setReferences(new ArrayList<>());
			for (String url : urls) {
				Reference reference = new Reference();
				try {
					reference.setUrl(new URL(url));
					reference.setWish(wish);
					wish.getReferences().add(reference);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		wishRepository.save(wish);
		
		return "redirect:/wishlist";
	}
	
	@PostMapping("/wish/update")
	public String editWish(Integer wishId, String description, String price, Model model, @ModelAttribute("messages") List<Message> messages) throws ParseException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		model.addAttribute("user", user);
		
		Optional<Wish> existing = wishRepository.findById(wishId);
		if (existing.isPresent()) {
			if (existing.get().getUser().equals(user)) {
				existing.get().setDescription(description);
				existing.get().setPrice(rangeParser.parse(price, Locale.getDefault()));
				wishRepository.save(existing.get());
				return "redirect:/wishlist";
			}
			messages.add(new Message("danger", "Error", "wishlist.update.notAuthorized"));
			return "secured/wishlist";
		}
		messages.add(new Message("danger", "Error", "wishlist.update.notFound"));
		return "secured/wishlist";
	}
	
	@PostMapping("/wish/delete")
	public String deleteWish(Integer wishId, Model model, @ModelAttribute("messages") List<Message> messages) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		model.addAttribute("user", user);
		
		Optional<Wish> wish = wishRepository.findById(wishId);
		if (wish.isPresent()) {
			if (wish.get().getUser().equals(user)) {
				wishRepository.delete(wish.get());
				return "redirect:/wishlist";
			}
			messages.add(new Message("danger", "Error", "wishlist.delete.notAuthorized"));
			return "secured/wishlist";
		}
		messages.add(new Message("danger", "Error", "wishlist.delete.notFound"));
		return "secured/wishlist";
	}
	
	@PostMapping("/wish/{id}/references")
	@ResponseBody
	public Reference addReference(@PathVariable(name = "id") Integer wishId, String url) throws MalformedURLException {
		Optional<Wish> wish = wishRepository.findById(wishId);
		if (wish.isPresent()) {
			Reference newRef = new Reference();
			newRef.setWish(wish.get());
			newRef.setUrl(new URL(url));
			wish.get().addReference(newRef);
			Wish updatedWish = wishRepository.save(wish.get());
			return updatedWish.getReferences().get(updatedWish.getReferences().size() - 1);
		}
		return null;
	}
	
	@PostMapping("/references/{id}/delete")
	@ResponseBody
	public String removeReference(@PathVariable(name = "id") Integer referenceId) {
		referenceRepository.deleteById(referenceId);
		return "OK";
	}
	
}
