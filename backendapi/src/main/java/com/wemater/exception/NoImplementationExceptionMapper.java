package com.wemater.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.wemater.modal.ErrorModel;

@Provider
public class NoImplementationExceptionMapper implements ExceptionMapper<NoImplementionException> {

	@Override
	public Response toResponse(NoImplementionException e) {
		ErrorModel model = new ErrorModel(e.getError_type(), e.getCode(), e.getError_message());
		return Response.status(Status.NOT_IMPLEMENTED).entity(model).build();
	}

}
