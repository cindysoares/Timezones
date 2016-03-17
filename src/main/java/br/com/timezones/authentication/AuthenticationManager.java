package br.com.timezones.authentication;

import java.util.StringTokenizer;

import javax.ws.rs.container.ContainerRequestContext;

import org.glassfish.jersey.internal.util.Base64;

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.UserDAO;
import br.com.timezones.model.User;

public class AuthenticationManager {
	
	private static final String LOGGED_USER_PROPERTY = "loggedUser";

	private static final String AUTHENTICATION_SCHEME = "Basic ";
	
	private static UserDAO userDAO = DAOFactory.getUserDAO();
	
	public static String generateToken(Credentials credentials) {
		User user = getUser(credentials.getEmail(), credentials.getPassword());
		if(user == null) {
        	return null;
		}
		return AUTHENTICATION_SCHEME + Base64.encodeAsString(credentials.getEmail() + ":" + credentials.getPassword());
	}
	
	public static User validateToken(String token, ContainerRequestContext requestContext) {
        String encodedUserPassword = token.replaceFirst(AUTHENTICATION_SCHEME, "");
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));

        StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();
        
        User user = getUser(username, password);
        requestContext.setProperty("loggedUser", user);		
		return user;
	}
	
	private static User getUser(String email, String password) {
		User user = userDAO.find(email);
		if(user == null || !password.equals(user.getPassword())) {
			return null;
		}
		return user;
	}
	
	public static User getLoggedUser(ContainerRequestContext context) {
		return (User) context.getProperty(LOGGED_USER_PROPERTY);
	}

}
