package br.com.timezones.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.UserDAO;
import br.com.timezones.model.User;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Login {
	
	private UserDAO dao = DAOFactory.getUserDAO();
	
	@GET
	@Path("/login")
	public User login(@QueryParam("email") String email, @QueryParam("password") String password) {
		User user = dao.find(email);
		if(user == null || !password.equals(user.getPassword())) {
			return null;
		}
		return user;
	}

}
