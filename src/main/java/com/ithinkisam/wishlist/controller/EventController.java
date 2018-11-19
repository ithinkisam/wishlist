package com.ithinkisam.wishlist.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
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
		
		Set<Event> events = new TreeSet<>(new Comparator<Event>() {

			@Override
			public int compare(Event o1, Event o2) {
				return o1.getDate().compareTo(o2.getDate());
			}
			
		});
		events.addAll(eventRepository.findByMembersId(user.getId()));
		events.addAll(eventRepository.findByAdminsId(user.getId()));
		model.addAttribute("events", events);
		
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
