package com.ithinkisam.wishlist.api.exception;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);
	
	@ExceptionHandler(PayloadException.class)
	public ResponseEntity<ErrorResponse> handlePayloadException(PayloadException e) {
		LOGGER.warn("Handling PayloadException", e);
		String message = messageSource.getMessage(e.getErrorCode(), new Object[] { e.getPayload() } , e.getErrorCode(), Locale.getDefault());
		return new ResponseEntity<ErrorResponse>(new ErrorResponse(message, e.getPayload()),
				e.getHttpStatus());
	}

}
