package com.wemater.exception;

public class NoImplementionException extends RuntimeException {

	private static final long serialVersionUID = -514701288935877604L;

	private String error_type;
	private int code;
	private String error_message;

	public NoImplementionException() {
		super();
		this.error_type = this.getClass().getSimpleName();
		this.code = 501;
		this.error_message = "Functionality not supported!";

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
