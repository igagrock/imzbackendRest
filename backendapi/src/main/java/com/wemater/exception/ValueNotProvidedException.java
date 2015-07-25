package com.wemater.exception;


public class ValueNotProvidedException extends RuntimeException {

	private static final long serialVersionUID = 8796853151248838077L;
	
	private String error_type;
	private int code;
	private String error_message;
	private String value_name;
	
	public ValueNotProvidedException(String error_message, String value_name) {
		super();
		this.error_message = error_message;
		this.value_name = value_name;
		this.code = 406;
		this.error_type = this.getClass().getSimpleName();
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
	public String getValue_name() {
		return value_name;
	}
	
	
	

	
	
	
	
}
