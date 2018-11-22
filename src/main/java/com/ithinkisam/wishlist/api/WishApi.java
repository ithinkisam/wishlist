package com.ithinkisam.wishlist.api;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Parser;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ithinkisam.wishlist.api.exception.ErrorCode;
import com.ithinkisam.wishlist.api.exception.PayloadException;
import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.model.Wish;
import com.ithinkisam.wishlist.model.support.Range;
import com.ithinkisam.wishlist.model.support.Reference;
import com.ithinkisam.wishlist.repository.UserRepository;
import com.ithinkisam.wishlist.repository.WishRepository;

@RestController
@RequestMapping("/api/wishes")
public class WishApi {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private WishRepository wishRepository;

	@Autowired
	private Parser<Range> rangeParser;
	
	@GetMapping
	public List<Wish> getAll() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		return user.getWishes();
	}

	@GetMapping("/{id}")
	public Wish getById(@PathVariable(name = "id") Integer id) {
		return wishRepository.findById(id)
				.orElseThrow(() -> new PayloadException(ErrorCode.WISH.notFound(), id, HttpStatus.NOT_FOUND));
	}
	
	@PostMapping
	public Wish create(String description, String price, String[] urls) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Wish wish = new Wish();
		wish.setDescription(description);
		try {
			wish.setPrice(rangeParser.parse(price, Locale.getDefault()));
		} catch (ParseException e1) {
			throw new PayloadException(ErrorCode.WISH.field("price").invalid(), price, HttpStatus.BAD_REQUEST);
		}
		wish.setUser(user);
		
		if (urls != null && urls.length > 0) {
			wish.setReferences(new ArrayList<>());
			for (String url : urls) {
				Reference reference = new Reference();
				try {
					reference.setUrl(new URL(url));
					wish.getReferences().add(reference);
				} catch (MalformedURLException e) {
					throw new PayloadException(ErrorCode.REFERENCE.invalid(), url, HttpStatus.BAD_REQUEST);
				}
			}
		}
		
		return wishRepository.save(wish);
	}
	
	@PutMapping("/{id}")
	public Wish update(@PathVariable(name = "id") Integer wishId, String description, String price) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Optional<Wish> existing = wishRepository.findById(wishId);
		if (existing.isPresent()) {
			if (existing.get().getUser().equals(user)) {
				existing.get().setDescription(description);
				try {
					existing.get().setPrice(rangeParser.parse(price, Locale.getDefault()));
				} catch (ParseException e) {
					throw new PayloadException(ErrorCode.WISH.field("price").invalid(), price, HttpStatus.BAD_REQUEST);
				}
				return wishRepository.save(existing.get());
			}
			throw new PayloadException(ErrorCode.WISH.notAuthorized(), existing, HttpStatus.BAD_REQUEST);
		}
		throw new PayloadException(ErrorCode.WISH.notFound(), wishId, HttpStatus.NOT_FOUND);
	}
	
	@DeleteMapping("/{id}")
	public void delete(@PathVariable(name = "id") Integer wishId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Optional<Wish> wish = wishRepository.findById(wishId);
		if (wish.isPresent()) {
			if (wish.get().getUser().equals(user)) {
				wishRepository.delete(wish.get());
				return;
			}
			throw new PayloadException(ErrorCode.WISH.notAuthorized(), wish, HttpStatus.BAD_REQUEST);
		}
		throw new PayloadException(ErrorCode.WISH.notFound(), wishId, HttpStatus.NOT_FOUND);
	}

}
