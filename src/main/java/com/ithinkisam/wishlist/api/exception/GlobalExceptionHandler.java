package com.ithinkisam.wishlist.api.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	@ExceptionHandler(PayloadException.class)
	public ResponseEntity<ErrorResponse> handlePayloadException(PayloadException e) {
		LOGGER.warn("Handling PayloadException", e);
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(e.getErrorCode(), e.getPayload()),
				e.getHttpStatus());
	}

}
