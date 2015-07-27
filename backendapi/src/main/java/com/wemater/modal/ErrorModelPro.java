package com.wemater.modal;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class ErrorModelPro {

	private String error_type;
	private int code;
	private String error_message;
	private String value_name;

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

	public void setError_type(String error_type) {
		this.error_type = error_type;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setError_message(String error_message) {
		this.error_message = error_message;
	}

	public void setValue_name(String value_name) {
		this.value_name = value_name;
	}

	public ErrorModelPro() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ErrorModelPro(String error_type, int code, String error_message,
			String value_name) {
		super();
		this.error_type = error_type;
		this.code = code;
		this.error_message = error_message;
		this.value_name = value_name;
	}

}
