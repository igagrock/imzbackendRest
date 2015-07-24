package com.wemater.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.wemater.modal.ErrorModel;

@Provider
public class DataNotInsertedExceptionMapper implements ExceptionMapper<DataNotInsertedException> {

	@Override
	public Response toResponse(DataNotInsertedException ex) {

		ErrorModel  errorModel = new ErrorModel(ex.getError_type(), ex.getCode(), ex.getError_message());
		System.out.println("Notintserted mapper executed..");
		return Response.status(Status.CONFLICT).entity(errorModel).build();
	}

}
