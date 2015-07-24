package com.wemater.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.wemater.modal.ErrorModelPro;

@Provider
public class ValueNotProvidedExceptionMapper implements ExceptionMapper<ValueNotProvidedException>{

	@Override
	public Response toResponse(ValueNotProvidedException ex) {
		ErrorModelPro edp = new ErrorModelPro(ex.getValuename(), ex.getMessage(), ex.getErrorcode(), "link for documentation");
		return Response.status(Status.NOT_FOUND).entity(edp).build();
		
		
	}

}
