package br.com.timezones.rest;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

import org.glassfish.jersey.message.internal.Statuses;

public class AppExceptionMapper implements ExceptionMapper<Exception> {
	
	private static int APP_EXCEPTION = 499;
	
	public Response toResponse(Exception ex) {		
		return Response.status(Statuses.from(APP_EXCEPTION, ex.getMessage()))
				.entity(ex)
				.type(MediaType.APPLICATION_JSON).build();
	}

}
