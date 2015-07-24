package com.wemater.modal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="error")
@XmlAccessorType(XmlAccessType.FIELD)
public class ErrorModelPro extends ErrorModel {

	private String valuename;

	

	public ErrorModelPro(String valuename,String message, String errorcode, String documentation) {
		super(message, errorcode, documentation);
		this.valuename = valuename;
		// TODO Auto-generated constructor stub
	}



	public String getValuename() {
		return valuename;
	}



	public void setValuename(String valuename) {
		this.valuename = valuename;
	}
	
	
	  
	  
	
}
