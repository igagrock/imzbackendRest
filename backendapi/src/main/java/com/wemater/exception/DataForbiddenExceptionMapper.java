package com.wemater.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.wemater.modal.ErrorModel;

@Provider
public class DataForbiddenExceptionMapper implements ExceptionMapper<DataForbiddenException> {

	@Override
	public Response toResponse(DataForbiddenException ex) {
		ErrorModel  errorModel = new ErrorModel(ex.getError_type(), ex.getCode(), ex.getError_message());
		System.out.println("DataForbidden mapper found");
		return Response.status(Status.FORBIDDEN).entity(errorModel).build();
	}

}
   