package com.ithinkisam.wishlist.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ithinkisam.wishlist.api.exception.ErrorCode;
import com.ithinkisam.wishlist.api.exception.PayloadException;
import com.ithinkisam.wishlist.model.Event;
import com.ithinkisam.wishlist.model.GiftExchangeAssignment;
import com.ithinkisam.wishlist.model.User;
import com.ithinkisam.wishlist.repository.EventRepository;
import com.ithinkisam.wishlist.repository.UserRepository;
import com.ithinkisam.wishlist.secretsanta.Rule;
import com.ithinkisam.wishlist.secretsanta.SecretSantaAssignmentCalculator;

@Controller
@RequestMapping("/events")
public class EventController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private EventRepository eventRepository;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(EventController.class);
	
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
	
	@GetMapping("/new")
	public String newEventPage(Model model) {
		model.addAttribute("event", new Event());
		return "secured/create-event";
	}
	
	@PostMapping
	public String createEvent(Event event) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		event.getAdmins().add(user);
		Event saved = eventRepository.save(event);
		
		return "redirect:/events/" + saved.getId();
	}
	
	@PostMapping("/{id}/update")
	public String updateEvent(Event event) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		Optional<Event> existing = eventRepository.findById(event.getId());
		if (existing.isPresent() && existing.get().getAdmins().contains(user)) {
			existing.get().setTitle(event.getTitle());
			existing.get().setDescription(event.getDescription());
			existing.get().setDate(event.getDate());
			existing.get().setLocation(event.getLocation());
			Event saved = eventRepository.save(existing.get());
			return "redirect:/events/" + saved.getId();
		}
		return "redirect:/events/" + event.getId();
	}
	
	@GetMapping("/{id}")
	public String getEvent(@PathVariable("id") Integer eventId, Model model) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		model.addAttribute("user", user);
		
		Optional<Event> event = eventRepository.findById(eventId);
		if (!event.isPresent()) {
			// do stuff
			// not found!
		}
		
		List<User> members = event.get().getMembers();
		if (!members.contains(user)) {
			// do stuff
			// not allowed!
		} else {
			Collections.sort(members, new Comparator<User>() {
				@Override
				public int compare(User o1, User o2) {
					return o1.getFirstName().compareTo(o2.getFirstName());
				}
			});
		}
		
		model.addAttribute("members", members);
		model.addAttribute("event", event.get());
		
		return "secured/event";
	}
	
	@PostMapping("/{id}/members")
	@ResponseBody
	public User addEventMember(@PathVariable("id") Integer eventId, @RequestParam("email") String email) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		LOGGER.debug("User {} attempting to add member to event {}", user.getId(), eventId);
		
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
			new PayloadException(ErrorCode.EVENT.notFound(), eventId, HttpStatus.NOT_FOUND)
		);
		if (!event.getAdmins().contains(user)) {
			LOGGER.debug("User {} is not an admin of event {} and cannot add members", user.getId(), eventId);
			throw new PayloadException(ErrorCode.USER.notAuthorized(), eventId, HttpStatus.BAD_REQUEST);
		}

		User newMember = userRepository.findByEmail(email);
		if (newMember == null) {
			throw new PayloadException(ErrorCode.USER.notFound(), email, HttpStatus.BAD_REQUEST);
		}
		LOGGER.debug("Found valid user candidate for event {} membership: user.id={}", eventId, newMember.getId());
		
		if (event.getMembers().contains(newMember)) {
			throw new PayloadException(ErrorCode.USER.exists(), newMember.getId(), HttpStatus.BAD_REQUEST);
		}
		
		event.getMembers().add(newMember);
		eventRepository.save(event);
		return newMember;
	}

	@PostMapping("/{id}/members/delete")
	@ResponseBody
	public String removeEventMember(@PathVariable("id") Integer eventId, @RequestParam("id") Integer userId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		LOGGER.debug("User {} attempting to remove member from event {}", user.getId(), eventId);
		
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
			new PayloadException(ErrorCode.EVENT.notFound(), eventId, HttpStatus.NOT_FOUND)
		);
		if (!event.getAdmins().contains(user)) {
			LOGGER.debug("User {} is not an admin of event {} and cannot remove members", user.getId(), eventId);
			throw new PayloadException(ErrorCode.USER.notAuthorized(), eventId, HttpStatus.BAD_REQUEST);
		}

		User memberToRemove = userRepository.findById(userId).orElseThrow(() ->
				new PayloadException(ErrorCode.USER.notFound(), userId, HttpStatus.BAD_REQUEST));
		LOGGER.debug("Found valid user candidate for event {} membership: user.id={}", eventId, memberToRemove.getId());
		
		if (!event.getMembers().contains(memberToRemove)) {
			throw new PayloadException(ErrorCode.USER.notFound(), memberToRemove.getId(), HttpStatus.BAD_REQUEST);
		}
		
		event.getMembers().remove(memberToRemove);
		eventRepository.save(event);
		return "OK";
	}

	@PostMapping("/{id}/admins")
	@ResponseBody
	public User addEventAdmin(@PathVariable("id") Integer eventId, @RequestParam("email") String email) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		LOGGER.debug("User {} attempting to add admin to event {}", user.getId(), eventId);
		
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
			new PayloadException(ErrorCode.EVENT.notFound(), eventId, HttpStatus.NOT_FOUND)
		);
		if (!event.getAdmins().contains(user)) {
			LOGGER.debug("User {} is not an admin of event {} and cannot add other admins", user.getId(), eventId);
			throw new PayloadException(ErrorCode.USER.notAuthorized(), eventId, HttpStatus.BAD_REQUEST);
		}

		User newAdmin = userRepository.findByEmail(email);
		if (newAdmin == null) {
			throw new PayloadException(ErrorCode.USER.notFound(), email, HttpStatus.BAD_REQUEST);
		}
		LOGGER.debug("Found valid user candidate for event {} adminstration: user.id={}", eventId, newAdmin.getId());
		
		if (event.getAdmins().contains(newAdmin)) {
			throw new PayloadException(ErrorCode.USER.exists(), newAdmin.getId(), HttpStatus.BAD_REQUEST);
		}
		
		event.getAdmins().add(newAdmin);
		eventRepository.save(event);
		return newAdmin;
	}

	@PostMapping("/{id}/admins/delete")
	@ResponseBody
	public String removeEventAdmins(@PathVariable("id") Integer eventId, @RequestParam("id") Integer userId) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		User user = userRepository.findByEmail(auth.getName());
		
		LOGGER.debug("User {} attempting to remove admin from event {}", user.getId(), eventId);
		
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
			new PayloadException(ErrorCode.EVENT.notFound(), eventId, HttpStatus.NOT_FOUND)
		);
		if (!event.getAdmins().contains(user)) {
			LOGGER.debug("User {} is not an admin of event {} and cannot remove admins", user.getId(), eventId);
			throw new PayloadException(ErrorCode.USER.notAuthorized(), eventId, HttpStatus.BAD_REQUEST);
		}

		User adminToRemove = userRepository.findById(userId).orElseThrow(() ->
				new PayloadException(ErrorCode.USER.notFound(), userId, HttpStatus.BAD_REQUEST));
		LOGGER.debug("Found valid user candidate for event {} administration: user.id={}", eventId, adminToRemove.getId());
		
		if (!event.getAdmins().contains(adminToRemove)) {
			throw new PayloadException(ErrorCode.USER.notFound(), adminToRemove.getId(), HttpStatus.BAD_REQUEST);
		}
		
		event.getAdmins().remove(adminToRemove);
		eventRepository.save(event);
		return "OK";
	}
	
	@PostMapping("/{id}/secret-santa")
	public String createSecretSanta(@PathVariable("id") Integer eventId,
			@RequestParam("ss-assignee") List<Integer> assignees,
			@RequestParam("ss-recipient") List<Integer> recipients) {
		
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
				new PayloadException(ErrorCode.EVENT.notFound(), eventId, HttpStatus.NOT_FOUND));
		
		Set<Rule<Integer>> exclusionRules = new HashSet<>();
		int ruleSize = Integer.min(assignees.size(), recipients.size());
		for (int i = 0; i < ruleSize; i++) {
			if (assignees.get(i) != null && recipients.get(i) != null) {
				exclusionRules.add(new Rule<Integer>(assignees.get(i), recipients.get(i)));
			}
		}
		
		Set<Integer> memberIds = event.getMembers().stream().map(e -> e.getId()).collect(Collectors.toSet());
		
		System.out.println("memberIds: " + memberIds);
		System.out.println("exclusionRules: " + exclusionRules);;
		
		SecretSantaAssignmentCalculator calculator = new SecretSantaAssignmentCalculator();
		Set<Rule<Integer>> calculatedAssignments = calculator.assign(memberIds, exclusionRules);
		
		System.out.println("calculatedAssignments: " + calculatedAssignments);
		
		event.getAssignments().clear();
		for (Rule<Integer> ssAssignment : calculatedAssignments) {
			GiftExchangeAssignment assignment = new GiftExchangeAssignment();
			assignment.setEvent(event);
			
			Optional<User> assignee = userRepository.findById(ssAssignment.getAssignee());
			Optional<User> recipient = userRepository.findById(ssAssignment.getRecipient());
			if (assignee.isPresent() && recipient.isPresent()) {
				assignment.setAssignee(assignee.get());
				assignment.setRecipient(recipient.get());
				event.getAssignments().add(assignment);
			}
		}
		eventRepository.save(event);
		
		return "redirect:/events/" + eventId + "?secretSantaAssigned";
	}
	
	@PostMapping("/{id}/secret-santa/reset")
	public String createSecretSanta(@PathVariable("id") Integer eventId) {
		Event event = eventRepository.findById(eventId).orElseThrow(() ->
				new PayloadException(ErrorCode.EVENT.notFound(), eventId, HttpStatus.NOT_FOUND));
		
		event.getAssignments().clear();
		eventRepository.save(event);
		
		return "redirect:/events/" + eventId + "?secretSantaReset";
	}
	
}
