package com.ithinkisam.wishlist.api.exception;

public class ErrorCode {

	private String base;
	
	private String delimiter = ".";
	
	public static final ErrorCode WISH = new ErrorCode("wish");
	public static final ErrorCode REFERENCE = new ErrorCode("reference");
	public static final ErrorCode EVENT = new ErrorCode("event");
	public static final ErrorCode USER = new ErrorCode("user");
	
	private static final String NOT_FOUND = "notFound";
	private static final String NOT_AUTHORIZED = "notAuthorized";
	private static final String INVALID = "invalid";
	private static final String EXISTS = "exists";
	
	public ErrorCode(String base) {
		this.base = base;
	}
	
	public String notFound() {
		return base + delimiter + NOT_FOUND; 
	}
	
	public String notAuthorized() {
		return base + delimiter + NOT_AUTHORIZED;
	}
	
	public String invalid() {
		return base + delimiter + INVALID;
	}

	public String exists() {
		return base + delimiter + EXISTS;
	}
	
	public ErrorCode field(String field) {
		return new ErrorCode(base + delimiter + field);
	}
	
}
