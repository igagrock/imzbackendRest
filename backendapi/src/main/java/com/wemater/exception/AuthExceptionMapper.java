package com.wemater.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.wemater.modal.ErrorModel;

@Provider
public class AuthExceptionMapper implements ExceptionMapper<AuthException> {

	@Override
	public Response toResponse(AuthException ex) {
		ErrorModel  errorModel = new ErrorModel(ex.getMessage(), ex.getErrorcode(), "some link here");
		System.out.println("AuthExceptionmapper mapper found");
		return Response.status(401).entity(errorModel).build();
	}

}
