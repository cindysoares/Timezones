package br.com.timezones.rest;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.timezones.authentication.AuthenticationManager;
import br.com.timezones.authentication.Credentials;
import br.com.timezones.model.User;

@Path("/login")
@Produces(MediaType.APPLICATION_JSON)
public class Login {
	
	@PermitAll
	@POST
	public Response login(Credentials credentials) {
		String token = AuthenticationManager.generateToken(credentials);
		if(token == null) {
        	return Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.ok(token).build();
	}
	
	@GET
	public User getLoggedUser(@Context ContainerRequestContext request) {
		return (User) request.getProperty("loggedUser");
	}

}
