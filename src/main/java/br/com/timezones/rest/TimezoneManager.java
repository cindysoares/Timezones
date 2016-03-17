package br.com.timezones.rest;

import java.util.List;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import br.com.timezones.authentication.AuthenticationManager;
import br.com.timezones.model.Profile;
import br.com.timezones.model.Timezone;
import br.com.timezones.model.User;

@Path("/timezones")
@RolesAllowed({"USER", "ADMIN_MANAGER"})
@Produces(MediaType.APPLICATION_JSON)
public class TimezoneManager {
	
	@GET
	public List<Timezone> findAll(@Context ContainerRequestContext request) {
		User loggedUser = AuthenticationManager.getLoggedUser(request);
		return getStrategy(loggedUser).findAll();
	}
	
	@POST
	public Response addTimezone(Timezone timezone, @Context ContainerRequestContext request) {
		try {
			User loggedUser = AuthenticationManager.getLoggedUser(request);
			Timezone addedTimezone = getStrategy(loggedUser).addTimezone(timezone); 
			return Response.ok().entity(addedTimezone).build();
		} catch(UnsupportedOperationException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	
	@PUT
	@Path("/{timezoneId}")
	public Response updateTimezone(@PathParam("timezoneId") Integer timezoneId, Timezone timezone, @Context ContainerRequestContext request) {		
		try {
			timezone.setId(timezoneId);
			User loggedUser = AuthenticationManager.getLoggedUser(request);
			Timezone updatedTimezone = getStrategy(loggedUser).updateTimezone(timezone);
			return Response.ok().entity(updatedTimezone).build();
		} catch(UnsupportedOperationException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}

	@DELETE
	@Path("/{timezoneId}")
	public Response removeTimezone(@PathParam("timezoneId") Integer timezoneId, @Context ContainerRequestContext request) {
		try {
			User loggedUser = AuthenticationManager.getLoggedUser(request);
			boolean removed = getStrategy(loggedUser).removeTimezone(timezoneId);
			return Response.ok().entity(removed).build();
		} catch(UnsupportedOperationException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		}
	}
	
	public TimezoneManagerStrategy getStrategy(User loggedUser) {
		if(Profile.USER.equals(loggedUser.getProfile())) {
			return new TimezoneManagerRegularUserStrategy(loggedUser);
		} else if (Profile.ADMIN_MANAGER.equals(loggedUser.getProfile())) {
			return new TimezoneManagerAdminStrategy(loggedUser);
		}
		
		throw new IllegalArgumentException("The profile " + loggedUser.getProfile() + " doesn't exist.");
	}

}
