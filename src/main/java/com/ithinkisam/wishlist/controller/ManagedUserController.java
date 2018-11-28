package com.ithinkisam.wishlist.controller;

import java.util.Iterator;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ithinkisam.wishlist.api.exception.ErrorCode;
import com.ithinkisam.wishlist.api.exception.PayloadException;
import com.ithinkisam.wishlist.model.Event;
import com.ithinkisam.wishlist.model.ManagedUser;
import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.repository.EventRepository;
import com.ithinkisam.wishlist.repository.ManagedUserRepository;
import com.ithinkisam.wishlist.repository.UserRepository;

@Controller
@RequestMapping("/managed-users")
public class ManagedUserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private ManagedUserRepository managedUserRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@GetMapping
	public String getManagedUsersPage(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		if (!user.getManagedUsers().isEmpty()) {
			return "redirect:/managed-users/" + user.getManagedUsers().get(0).getId();
		} else {
			return "redirect:/managed-users/new";
		}
	}

	@GetMapping("/new")
	public String getNewManagedUsersPage(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		model.addAttribute("managedUser", new ManagedUser());
		model.addAttribute("newManagedUser", new ManagedUser());
		model.addAttribute("events", eventRepository.findByMembersId(user.getId()));
		
		return "secured/managed-user";
	}
	
	@GetMapping("/{id}")
	public String getManagedUserPage(@PathVariable("id") Integer managedUserId, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		for (ManagedUser managedUser : user.getManagedUsers()) {
			if (managedUserId == managedUser.getId()) {
				model.addAttribute("managedUser", managedUser);
			}
		}
		model.addAttribute("newManagedUser", new ManagedUser());
		model.addAttribute("events", eventRepository.findByMembersId(user.getId()));
		model.addAttribute("guardians", userRepository.findByManagedUsersId(managedUserId));
		
		if (!model.containsAttribute("managedUser")) {
			return "redirect:/managed-users/new";
		}
		
		return "secured/managed-user";
	}
	
	@PostMapping
	public String createManagedUser(@Valid ManagedUser managedUser, BindingResult bindingResult) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		if (bindingResult.hasErrors()) {
			return "secured/managed-user";
		}
		
		ManagedUser inserted = managedUserRepository.save(managedUser);
		
		user.getManagedUsers().add(inserted);
		userRepository.save(user);
		
		return "redirect:/managed-users/" + inserted.getId();
	}
	
	@PostMapping("/{id}/update")
	public String updateManagedUser(ManagedUser managedUser) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		for (ManagedUser existing : user.getManagedUsers()) {
			if (existing.getId() == managedUser.getId()) {
				existing.setFirstName(managedUser.getFirstName());
				existing.setLastName(managedUser.getLastName());
				userRepository.save(user);
				break;
			}
		}
		
		return "redirect:/managed-users/" + managedUser.getId();
	}
	
	@PostMapping("/{id}/delete")
	public String deleteManagedUser(@PathVariable("id") Integer managedUserId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Iterator<ManagedUser> itr = user.getManagedUsers().iterator();
		while (itr.hasNext()) {
			ManagedUser next = itr.next();
			if (managedUserId.equals(next.getId())) {
				itr.remove();
				userRepository.save(user);
				managedUserRepository.deleteById(managedUserId);
				break;
			}
		}
		return "redirect:/managed-users";
	}
	
	@PostMapping("/{id}/event")
	public String addManagedUserToEvent(@PathVariable("id") Integer managedUserId,
			@RequestParam("eventId") Integer eventId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Iterator<ManagedUser> itr = user.getManagedUsers().iterator();
		while (itr.hasNext()) {
			ManagedUser next = itr.next();
			if (managedUserId.equals(next.getId())) {
				Event event = eventRepository.findById(eventId).orElseThrow(() -> 
						new PayloadException(ErrorCode.EVENT.notFound(), eventId, HttpStatus.NOT_FOUND));
				event.getManagedUsers().add(next);
				eventRepository.save(event);
				break;
			}
		}
		return "redirect:/managed-users/" + managedUserId;
	}
	
	@PostMapping("/{id}/event/remove")
	public String removeManagedUserFromEvent(@PathVariable("id") Integer managedUserId,
			@RequestParam("eventId") Integer eventId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Iterator<ManagedUser> itr = user.getManagedUsers().iterator();
		while (itr.hasNext()) {
			ManagedUser next = itr.next();
			if (managedUserId.equals(next.getId())) {
				Event event = eventRepository.findById(eventId).orElseThrow(() -> 
						new PayloadException(ErrorCode.EVENT.notFound(), eventId, HttpStatus.NOT_FOUND));
				event.getManagedUsers().remove(next);
				eventRepository.save(event);
				break;
			}
		}
		return "redirect:/managed-users/" + managedUserId;
	}
	
	@PostMapping("/{id}/user")
	public String addManager(@PathVariable("id") Integer managedUserId,
			@RequestParam("email") String email) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		User guardian = userRepository.findByEmail(email);
		if (guardian == null) {
			return "redirect:/managed-users/" + managedUserId + "/?guardianNotFound";
		}
		
		for (ManagedUser existing : user.getManagedUsers()) {
			if (existing.getId() == managedUserId) {
				guardian.getManagedUsers().add(existing);
				userRepository.save(guardian);
				break;
			}
		}
		return "redirect:/managed-users/" + managedUserId;
	}
	
	@PostMapping("/{id}/user/remove")
	public String addManager(@PathVariable("id") Integer managedUserId,
			@RequestParam("userId") Integer guardianId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Optional<User> guardian = userRepository.findById(guardianId);
		if (!guardian.isPresent()) {
			return "redirect:/managed-users/" + managedUserId + "/?guardianNotFound";
		}
		
		for (ManagedUser existing : user.getManagedUsers()) {
			if (existing.getId() == managedUserId) {
				guardian.get().getManagedUsers().remove(existing);
				userRepository.save(guardian.get());
				break;
			}
		}
		return "redirect:/managed-users/" + managedUserId;
	}
	
}
