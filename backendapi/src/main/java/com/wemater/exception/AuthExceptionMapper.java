package com.wemater.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.wemater.modal.ErrorModel;

@Provider
public class AuthExceptionMapper implements ExceptionMapper<AuthException> {

	@Override
	public Response toResponse(AuthException ex) {
		ErrorModel errorModel = new ErrorModel(ex.getError_type(),
				ex.getCode(), ex.getError_message());
		System.out.println("AuthExceptionmapper mapper found");
		return Response.status(Status.UNAUTHORIZED).entity(errorModel).build();
	}

}
