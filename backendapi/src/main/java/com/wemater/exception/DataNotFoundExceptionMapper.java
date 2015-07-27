package com.wemater.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.wemater.modal.ErrorModel;

@Provider
public class DataNotFoundExceptionMapper implements
		ExceptionMapper<DataNotFoundException> {

	@Override
	public Response toResponse(DataNotFoundException ex) {

		ErrorModel errorModel = new ErrorModel(ex.getError_type(),
				ex.getCode(), ex.getError_message());
		System.out.println("NotFound mapper executed..");
		return Response.status(Status.NOT_FOUND).entity(errorModel).build();
	}

}
