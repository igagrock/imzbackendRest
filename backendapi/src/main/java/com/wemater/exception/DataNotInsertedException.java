package com.wemater.exception;

public class DataNotInsertedException extends RuntimeException {

	private static final long serialVersionUID = 1497666190967016492L;
	private String error_type;
	private int code;
	private String error_message;

	public DataNotInsertedException(String error_message) {
		super();
		this.error_message = error_message;
		this.code = 409;
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
