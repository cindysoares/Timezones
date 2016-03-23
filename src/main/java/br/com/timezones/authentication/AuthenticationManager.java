package br.com.timezones.authentication;

import java.security.Key;
import java.util.Date;

import javax.ws.rs.container.ContainerRequestContext;

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.UserDAO;
import br.com.timezones.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.impl.crypto.MacProvider;

public class AuthenticationManager {
	
	private static Key signingKey = MacProvider.generateKey();
	
	private static long timeToExpire = 1800000; // 30 minutes
	
	private static final String LOGGED_USER_PROPERTY = "loggedUser";
	
	private static UserDAO userDAO = DAOFactory.getUserDAO();
	
	public static String generateToken(Credentials credentials) {
		User user = validateCredentials(credentials.getEmail(), credentials.getPassword());
		if(user == null) {
        	return null;
		}
		Date loggingTime = new Date();
		
		JwtBuilder builder = Jwts.builder().setId(user.getEmail())
                .setIssuedAt(loggingTime)
                .signWith(SignatureAlgorithm.HS256, signingKey)
                .setExpiration(new Date(loggingTime.getTime()+timeToExpire));
		return builder.compact();
	}
	
	public static User validateToken(String token, ContainerRequestContext requestContext) {
		try {
			Claims claims = Jwts.parser()         
					.setSigningKey(signingKey)
					.parseClaimsJws(token).getBody();

			String email = claims.getId();
			Date expiration = claims.getExpiration();
			if(expiration == null || expiration.before(new Date())) {
				return null;
			} 

			User user = getUser(email);
			requestContext.setProperty(LOGGED_USER_PROPERTY, user);
			return user;
		} catch (SignatureException e) {
			return null;
		}
	}
	
	private static User getUser(String email) {
		return userDAO.find(email);
	}
	
	private static User validateCredentials(String email, String password) {
		User user = getUser(email);
		if(user == null || !password.equals(user.getPassword())) {
			return null;
		}
		return user;
	}
	
	public static User getLoggedUser(ContainerRequestContext context) {
		return (User) context.getProperty(LOGGED_USER_PROPERTY);
	}

}
