package com.wemater.exception;

public class AuthException extends RuntimeException {
	
	
	private static final long serialVersionUID = -955486763315159244L;
	public String errorcode;
	  public String message;
	
	public AuthException(String errorcode,String message) {
			this.errorcode = errorcode;
			this.message = message;
			
	}

	
	public String getErrorcode(){
		return this.errorcode;
	}
	
	
	@Override
	public String getMessage() {
			return this.message;
	}
	
}
