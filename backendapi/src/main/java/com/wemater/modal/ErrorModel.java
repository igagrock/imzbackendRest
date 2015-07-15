package com.wemater.modal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorModel {
	
	private String message;
	private String errorcode;
	private String documentation;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}
	public String getDocumentation() {
		return documentation;
	}
	public void setDocumentation(String documentation) {
		this.documentation = documentation;
	}
	
	public ErrorModel(String message, String errorcode, String documentation) {
		super();
		this.message = message;
		this.errorcode = errorcode;
		this.documentation = documentation;
	}
	public ErrorModel() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	

}
