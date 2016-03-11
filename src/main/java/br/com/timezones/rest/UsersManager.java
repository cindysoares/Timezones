package br.com.timezones.rest;

import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.UserDAO;
import br.com.timezones.model.Profile;
import br.com.timezones.model.User;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersManager {

	private UserDAO dao = DAOFactory.getUserDAO();
	
	@POST
	@Path("/add")
	public User addUser(@QueryParam("name") String name, @QueryParam("email") String email,
			@QueryParam("password") String password, @QueryParam("profile") String profileName) {
		Profile profile = getProfile(profileName);
		User user = new User(name, email, password, profile);
		return dao.save(user);
	}

	@POST
	@Path("/update/{userId}")
	public User updateUser(@PathParam("userId") Integer userId, 
			@QueryParam("name") String name, @QueryParam("email") String email, 
			@QueryParam("password")	String password, @QueryParam("profile") String profileName) {
		return dao.update(new User(userId, name, email, password, getProfile(profileName)));
	}

	@DELETE
	@Path("/remove/{userId}")
	public boolean removeUser(@PathParam("userId") Integer userId) {
		return dao.remove(userId);
	}

	private Profile getProfile(String profileName) {
		Profile profile = null;
		if(profileName != null) {
			try {
				profile = Profile.valueOf(profileName.toUpperCase());
			} catch(IllegalArgumentException ignored) {}
		}
		return profile;
	}
	
	@GET
	@Path("/all")
	public Set<User> getAll() {
		return dao.findAll();
	}

}