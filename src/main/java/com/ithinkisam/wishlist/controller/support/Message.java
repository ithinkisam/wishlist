package com.ithinkisam.wishlist.controller.support;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Message {

	private String type;
	
	private String heading;
	
	private String content;
	
}
