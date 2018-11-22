package com.ithinkisam.wishlist.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ithinkisam.wishlist.api.exception.ErrorCode;
import com.ithinkisam.wishlist.api.exception.PayloadException;
import com.ithinkisam.wishlist.model.Event;
import com.ithinkisam.wishlist.repository.EventRepository;

@RestController
@RequestMapping("/api/events")
public class EventApi {

	@Autowired
	private EventRepository eventRepository;
	
	@GetMapping("/{id}")
	public Event getById(@PathVariable("id") Integer id) {
		return eventRepository.findById(id).orElseThrow(() ->
				new PayloadException(ErrorCode.EVENT.notFound(), id, HttpStatus.NOT_FOUND));
	}
	
}
