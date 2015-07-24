package com.wemater.modal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorModelPro  {

	private String valuename;
	private String message;
	private String errorcode;
	private String documentation;


	public ErrorModelPro() {}
		

	public ErrorModelPro(String valuename, String message,String errorcode	) {
		
		super();
		this.valuename = valuename;
		this.message = message;
		this.errorcode = errorcode;
		this.documentation = "Some documentation link here";
	}


	public String getValuename() {
		return valuename;
	}

	public void setValuename(String valuename) {
		this.valuename = valuename;
	}

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


	
	  
	  
	
}
