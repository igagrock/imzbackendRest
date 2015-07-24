package com.wemater.exception;

public class ValueNotProvidedException extends RuntimeException {

	private static final long serialVersionUID = 8796853151248838077L;
	
	private String valuename;
	private String errorcode;
	private String message;
	
	public ValueNotProvidedException(String valuename, String message) {
		super();
		this.valuename = valuename;
		this.errorcode = "404";
		this.message = message;
	}
	public String getValuename() {
		return valuename;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public String getMessage() {
		return message;
	}

	
	
	
	
}
