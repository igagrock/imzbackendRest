package com.wemater.exception;

public class DataNotFoundException extends RuntimeException{

	private static final long serialVersionUID = 1497666190967016492L;
    public String errorcode;
	  public String message;
	
	public DataNotFoundException(String errorcode,String message) {
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
