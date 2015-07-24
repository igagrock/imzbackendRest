package com.wemater.exception;


public class AuthException extends RuntimeException {
	
	
	private static final long serialVersionUID = -955486763315159244L;
	private String error_type;
	private int code;
	private String error_message;
	public AuthException(String error_message) {
		super();
		this.error_type = this.getClass().getSimpleName();
		this.code = 401;
		this.error_message = error_message;
		
		
	}
	public String getError_type() {
		return error_type;
	}
	public int getCode() {
		return code;
	}
	public String getError_message() {
		return error_message;
	}

	
	
	
}
