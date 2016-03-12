package br.com.timezones.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.TimezoneDAO;
import br.com.timezones.dao.UserDAO;
import br.com.timezones.model.Profile;
import br.com.timezones.model.Timezone;
import br.com.timezones.model.User;

@Path("/timezones")
@Produces(MediaType.APPLICATION_JSON)
public class TimezoneManager {
	
	private UserDAO userDAO = DAOFactory.getUserDAO();
	private TimezoneDAO timezoneDAO = DAOFactory.getTimezoneDAO();
	
	@GET
	@Path("/{userId}")
	public List<Timezone> findAll(@PathParam("userId") Integer userId) {
		User user = userDAO.find(userId);
		if(Profile.ADMIN_MANAGER.equals(user.getProfile())) {
			return timezoneDAO.findAll();
		} else if(Profile.USER.equals(user.getProfile())) {
			return timezoneDAO.findByUser(userId);
		} 
		
		throw new UnsupportedOperationException();
	}
	
	@POST
	@Path("/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Timezone addTimezone(@PathParam("userId") Integer userId, Timezone timezone) {
		User user = userDAO.find(userId);
		if(user == null) return null;
		if(user != null) {
			timezone = timezoneDAO.save(timezone);
		}
		return timezone;
	}
	
	@PUT
	@Path("/{userId}/{timezoneId}")
	public Timezone updateTimezone(@PathParam("userId") Integer userId, @PathParam("timezoneId") Integer timezoneId, 
			Timezone timezone) {
		User user = userDAO.find(userId);
		timezone.setId(timezoneId);
		return timezoneDAO.update(timezone);
	}

	@DELETE
	@Path("/remove/{userId}/{timezoneId}")
	public boolean removeTimezone(@PathParam("userId") Integer userId, @PathParam("timezoneId") Integer timezoneId) {
		User user = userDAO.find(userId);
		return timezoneDAO.remove(timezoneId);
	}

}
