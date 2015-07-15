package com.wemater.exception;

public class DataForbiddenException extends RuntimeException{

	private static final long serialVersionUID = 3712701104779850835L;

	public String errorcode;
	  public String message;
	
	public DataForbiddenException(String errorcode,String message) {
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
