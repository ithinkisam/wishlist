package com.ithinkisam.wishlist.api.exception;

import org.springframework.http.HttpStatus;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class PayloadException extends RuntimeException {

	private static final long serialVersionUID = -7485049248445086158L;

	private String errorCode;
	
	private Object payload;
	
	private HttpStatus httpStatus;
	
	public PayloadException(String errorCode, Object payload, HttpStatus httpStatus) {
		this.errorCode = errorCode;
		this.payload = payload;
		this.httpStatus = httpStatus;
	}
	
}
