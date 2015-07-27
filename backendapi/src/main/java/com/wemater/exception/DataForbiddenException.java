package com.wemater.exception;

public class DataForbiddenException extends RuntimeException {

	private static final long serialVersionUID = 3712701104779850835L;
	private String error_type;
	private int code;
	private String error_message;

	public DataForbiddenException(String error_message) {
		super();
		this.error_message = error_message;
		this.code = 403;
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

}
