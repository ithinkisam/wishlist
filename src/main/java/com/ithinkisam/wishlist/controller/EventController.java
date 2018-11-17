package com.ithinkisam.wishlist.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ithinkisam.wishlist.model.Event;
import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.repository.EventRepository;
import com.ithinkisam.wishlist.repository.UserRepository;

@Controller
@RequestMapping("/events")
public class EventController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	@GetMapping
	public String getEvents(Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		model.addAttribute("memberEvents", eventRepository.findByMembersId(user.getId()));
		model.addAttribute("adminEvents", eventRepository.findByAdminsId(user.getId()));
		
		return "secured/events";
	}
	
	@GetMapping("/{id}")
	public String getEvent(@PathVariable("id") Integer eventId, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Optional<Event> event = eventRepository.findById(eventId);
		if (!event.isPresent()) {
			// do stuff
		}
		
		if (!event.get().getMembers().stream().map(User::getId).collect(Collectors.toList()).contains(user.getId())
				&& !event.get().getAdmins().stream().map(User::getId).collect(Collectors.toList()).contains(user.getId())) {
			model.addAttribute("event", event.get());
		} else {
			// no access to event
		}
		
		model.addAttribute("event", event.get());
		
		return "secured/event";
	}
	
}
