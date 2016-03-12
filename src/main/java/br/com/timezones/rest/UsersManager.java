package br.com.timezones.rest;

import java.util.Set;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.UserDAO;
import br.com.timezones.model.User;

@Path("/users")
@Produces(MediaType.APPLICATION_JSON)
public class UsersManager {

	private UserDAO dao = DAOFactory.getUserDAO();
	
	@POST
	@Path("/")
	public User addUser(User user) {
		return dao.save(user);
	}

	@PUT
	@Path("/{userId}")
	public User updateUser(@PathParam("userId") Integer userId, User user) {
		user.setId(userId);
		return dao.update(user);
	}

	@DELETE
	@Path("/{userId}")
	public boolean removeUser(@PathParam("userId") Integer userId) {
		return dao.remove(userId);
	}

	@GET
	@Path("/")
	public Set<User> getAll() {
		return dao.findAll();
	}

}
