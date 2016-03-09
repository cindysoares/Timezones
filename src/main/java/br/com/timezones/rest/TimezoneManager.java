package br.com.timezones.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.DELETE;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import br.com.timezones.dao.MemoryUserDAO;
import br.com.timezones.dao.UserDAO;
import br.com.timezones.model.Profile;
import br.com.timezones.model.Timezone;
import br.com.timezones.model.User;

@Path("/timezones")
@Produces(MediaType.APPLICATION_JSON)
public class TimezoneManager {
	
	private UserDAO dao = new MemoryUserDAO();
	
	@POST
	@Path("/{userId}")
	public List<Timezone> findAll(@PathParam("userId") Integer userId) {
		User user = dao.find(userId);
		if(Profile.ADMIN_MANAGER.equals(user.getProfile())) {
			return new ArrayList<Timezone>();
		} else if(Profile.USER.equals(user.getProfile())) {
			return user.getTimezones();
		} 
		
		throw new UnsupportedOperationException();
	}
	
	@POST
	@Path("/add/{userId}")
	public Timezone addTimezone(@PathParam("userId") Integer userId, 
			@QueryParam("name") String name, @QueryParam("city") String city, @QueryParam("gmtDifference") Integer gmtDifference) {
		User user = dao.find(userId);
		Timezone timezone = null;
		if(user != null) {
			timezone = new Timezone(name, city, gmtDifference);
			user.addTimezone(timezone);
		}
		return timezone;
	}
	
	@POST
	@Path("/update/{userId}/{timezoneId}")
	@Produces(MediaType.APPLICATION_JSON)
	public Timezone updateTimezone(@PathParam("userId") Integer userId, @PathParam("timezoneId") Integer timezoneId, 
			@QueryParam("name") String name, @QueryParam("city") String city, @QueryParam("gmtDifference") Integer gmtDifference) {
		User user = dao.find(userId);
		Timezone timezone = user.getTimezones().stream().filter(m -> m.getId().equals(timezoneId)).findFirst().orElse(null);
		if(timezone != null) {
			timezone.setName(name);
			timezone.setCity(city);
			timezone.setGmtDifference(gmtDifference);
		}
		return timezone;
	}

	@DELETE
	@Path("/remove/{userId}/{timezoneId}")
	@Produces(MediaType.APPLICATION_JSON)
	public boolean removeTimezone(@PathParam("userId") Integer userId, @PathParam("timezoneId") Integer timezoneId) {
		User user = dao.find(userId);
		return user.getTimezones().removeIf(m -> m.getId().equals(timezoneId));
	}

}
