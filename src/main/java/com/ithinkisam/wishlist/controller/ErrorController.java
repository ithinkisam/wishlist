package com.ithinkisam.wishlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

	@GetMapping("/404")
	public String error404() {
		return "404";
	}
	
	@GetMapping("/error")
	public String error() {
		return "error";
	}
	
}
