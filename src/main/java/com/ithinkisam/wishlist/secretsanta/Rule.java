package com.ithinkisam.wishlist.secretsanta;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rule<T> {

	private T assignee;
	
	private T recipient;
	
}
