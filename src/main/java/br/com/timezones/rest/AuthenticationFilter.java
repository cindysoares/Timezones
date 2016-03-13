package br.com.timezones.rest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import org.glassfish.jersey.internal.util.Base64;

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.UserDAO;
import br.com.timezones.model.User;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;
	
	private UserDAO userDAO = DAOFactory.getUserDAO();

	public static final String AUTHORIZATION_PROPERTY = "Authorization";
    private static final String AUTHENTICATION_SCHEME = "Basic";
    
	private static final Response ACCESS_DENIED = Response.status(Response.Status.UNAUTHORIZED)
			.entity("You cannot access this resource").build();
	private static final Response ACCESS_FORBIDDEN = Response.status(Response.Status.FORBIDDEN)
			.entity("Access blocked for all users !!").build();

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Method method = resourceInfo.getResourceMethod();

        if(!method.isAnnotationPresent(PermitAll.class)) {
        	if(method.isAnnotationPresent(DenyAll.class)) {
                requestContext.abortWith(ACCESS_FORBIDDEN);
                return;
            }
        }
        
        final List<String> authorization = requestContext.getHeaders().get(AUTHORIZATION_PROPERTY);
        if(authorization == null || authorization.isEmpty())
        {
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }
          
        String encodedUserPassword = authorization.get(0).replaceFirst(AUTHENTICATION_SCHEME + " ", "");
        String usernameAndPassword = new String(Base64.decode(encodedUserPassword.getBytes()));

        StringTokenizer tokenizer = new StringTokenizer(usernameAndPassword, ":");
        String username = tokenizer.nextToken();
        String password = tokenizer.nextToken();
        
        User user = getUser(username, password);
        if(user == null) {
        	requestContext.abortWith(ACCESS_DENIED);
            return;
        }
        
        if(method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
              
            if(!isUserAllowed(user, rolesSet)) {
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }
        }        
	}
	
	private User getUser(String email, String password) {
		User user = userDAO.find(email);
		if(user == null || !password.equals(user.getPassword())) {
			return null;
		}
		return user;
	}
	
	private boolean isUserAllowed(User user, final Set<String> rolesSet) {		
		if(user == null || rolesSet.contains(user.getProfile().name())) {
			return false;
		}		
		return true;
	}

}
