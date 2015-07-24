package com.wemater.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.wemater.modal.ErrorModel;

@Provider
public class DataNotInsertedExceptionMapper implements ExceptionMapper<DataNotInsertedException> {

	@Override
	public Response toResponse(DataNotInsertedException ex) {

		ErrorModel  errorModel = new ErrorModel(ex.getMessage(), ex.getErrorcode(), "some link here");
		
		System.out.println("Notintserted mapper executed..");
		return Response.status(400).entity(errorModel).build();
	}

}
