package com.ithinkisam.wishlist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/secret-santa")
public class SecretSantaController {

	@GetMapping
	public String secretSanta() {
		return "secured/secret-santa";
	}
}
