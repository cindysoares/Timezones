package br.com.timezones.rest;

import java.util.List;

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

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.TimezoneDAO;
import br.com.timezones.model.Profile;
import br.com.timezones.model.Timezone;
import br.com.timezones.model.User;

@Path("/timezones")
@Produces(MediaType.APPLICATION_JSON)
public class TimezoneManager {
	
	private TimezoneDAO timezoneDAO = DAOFactory.getTimezoneDAO();
	
	@GET
	public List<Timezone> findAll(@Context ContainerRequestContext request) {
		User user = (User) request.getProperty("loggedUser");
		if(Profile.ADMIN_MANAGER.equals(user.getProfile())) {
			return timezoneDAO.findAll();
		} else if(Profile.USER.equals(user.getProfile())) {
			return timezoneDAO.findByUser(user.getId());
		} 
		
		throw new UnsupportedOperationException();
	}
	
	@POST
	public Timezone addTimezone(Timezone timezone) {
		return timezoneDAO.save(timezone);
	}
	
	@PUT
	@Path("/{timezoneId}")
	public Timezone updateTimezone(@PathParam("timezoneId") Integer timezoneId, Timezone timezone) {
		timezone.setId(timezoneId);
		return timezoneDAO.update(timezone);
	}

	@DELETE
	@Path("/{timezoneId}")
	public boolean removeTimezone(@PathParam("timezoneId") Integer timezoneId) {
		return timezoneDAO.remove(timezoneId);
	}

}
