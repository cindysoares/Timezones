package br.com.timezones.rest;

import javax.annotation.security.PermitAll;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.UserDAO;
import br.com.timezones.model.User;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class Login {
	
	private UserDAO dao = DAOFactory.getUserDAO();
	
	@PermitAll
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public User login(@FormParam("email") String email, @FormParam("password") String password) {
		System.out.println(email + ":" + password);
		User user = dao.find(email);
		if(user == null || !password.equals(user.getPassword())) {
        	return null;
		}
		return user;
	}

}
