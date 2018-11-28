package com.ithinkisam.wishlist.controller;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Parser;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ithinkisam.wishlist.api.exception.ErrorCode;
import com.ithinkisam.wishlist.api.exception.PayloadException;
import com.ithinkisam.wishlist.model.ManagedReference;
import com.ithinkisam.wishlist.model.ManagedUser;
import com.ithinkisam.wishlist.model.ManagedWish;
import com.ithinkisam.wishlist.model.support.Range;
import com.ithinkisam.wishlist.repository.ManagedReferenceRepository;
import com.ithinkisam.wishlist.repository.ManagedUserRepository;
import com.ithinkisam.wishlist.repository.ManagedWishRepository;

@Controller
@RequestMapping("/managed-wishes")
public class ManagedWishController {

	@Autowired
	private ManagedUserRepository managedUserRepository;

	@Autowired
	private ManagedWishRepository managedWishRepository;
	
	@Autowired
	private ManagedReferenceRepository managedReferenceRepository;
	
	@Autowired
	private Parser<Range> rangeParser;

	@GetMapping("/{id}")
	@ResponseBody
	public ManagedWish getWish(@PathVariable("id") Integer wishId) {
		return managedWishRepository.findById(wishId).orElse(new ManagedWish());
	}
	
	@PostMapping("/{userId}")
	public String addWish(@PathVariable("userId") Integer managedUserId, String description,
			@RequestParam(name = "urls", defaultValue = "") String[] urls,
			@RequestParam(name = "price", required = false) String price) throws ParseException {
		ManagedUser managedUser = managedUserRepository.findById(managedUserId).orElseThrow(() ->
				new PayloadException(ErrorCode.USER.notFound(), managedUserId, HttpStatus.NOT_FOUND));
		
		ManagedWish wish = new ManagedWish();
		wish.setDescription(description);
		wish.setPrice(rangeParser.parse(price, Locale.getDefault()));
		wish.setPurchased(false);
		wish.setUser(managedUser);
		
		if (urls != null && urls.length > 0) {
			wish.setReferences(new ArrayList<>());
			for (String url : urls) {
				ManagedReference reference = new ManagedReference();
				try {
					reference.setUrl(new URL(url));
					reference.setWish(wish);
					wish.getReferences().add(reference);
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		managedWishRepository.save(wish);
		return "redirect:/managed-users/" + managedUserId;
	}
	
	@PostMapping("/{userId}/update")
	public String editWish(@PathVariable("userId") Integer managedUserId,
			@RequestParam("wishId") Integer wishId, String description, String price) throws ParseException {
		ManagedUser user = managedUserRepository.findById(managedUserId).orElseThrow(() ->
				new PayloadException(ErrorCode.USER.notFound(), managedUserId, HttpStatus.NOT_FOUND));
		
		Optional<ManagedWish> existing = managedWishRepository.findById(wishId);
		if (existing.isPresent()) {
			if (existing.get().getUser().equals(user)) {
				existing.get().setDescription(description);
				existing.get().setPrice(rangeParser.parse(price, Locale.getDefault()));
				managedWishRepository.save(existing.get());
				return "redirect:/managed-users/" + managedUserId;
			}
			throw new PayloadException(ErrorCode.WISH.notAuthorized(), wishId, HttpStatus.BAD_REQUEST);
		}
		throw new PayloadException(ErrorCode.WISH.notFound(), wishId, HttpStatus.NOT_FOUND);
	}
	
	@PostMapping("/{userId}/delete")
	public String deleteWish(@PathVariable("userId") Integer managedUserId,
			@RequestParam("wishId") Integer wishId) {
		ManagedUser user = managedUserRepository.findById(managedUserId).orElseThrow(() ->
				new PayloadException(ErrorCode.USER.notFound(), managedUserId, HttpStatus.NOT_FOUND));
		
		Optional<ManagedWish> wish = managedWishRepository.findById(wishId);
		if (wish.isPresent()) {
			if (wish.get().getUser().equals(user)) {
				managedWishRepository.delete(wish.get());
				return "redirect:/managed-users/" + managedUserId;
			}
			throw new PayloadException(ErrorCode.WISH.notAuthorized(), wishId, HttpStatus.BAD_REQUEST);
		}
		throw new PayloadException(ErrorCode.WISH.notFound(), wishId, HttpStatus.NOT_FOUND);
	}

	@PostMapping("/{wishId}/references")
	@ResponseBody
	public ManagedReference addReference(@PathVariable(name = "wishId") Integer wishId, String url) throws MalformedURLException {
		Optional<ManagedWish> wish = managedWishRepository.findById(wishId);
		if (wish.isPresent()) {
			ManagedReference newRef = new ManagedReference();
			newRef.setWish(wish.get());
			newRef.setUrl(new URL(url));
			wish.get().addReference(newRef);
			ManagedWish updatedWish = managedWishRepository.save(wish.get());
			return updatedWish.getReferences().get(updatedWish.getReferences().size() - 1);
		}
		return null;
	}
	
	@PostMapping("/references/{referenceId}/delete")
	@ResponseBody
	public String removeReference(@PathVariable(name = "referenceId") Integer referenceId) {
		managedReferenceRepository.deleteById(referenceId);
		return "OK";
	}
	
}
