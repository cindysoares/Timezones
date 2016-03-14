package br.com.timezones.rest;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.security.DenyAll;
import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import br.com.timezones.authentication.AuthenticationManager;
import br.com.timezones.model.User;

@Provider
public class AuthenticationFilter implements ContainerRequestFilter {

	@Context
	private ResourceInfo resourceInfo;

	public static final String AUTHORIZATION_PROPERTY = "Authorization";

	@Override
	public void filter(ContainerRequestContext requestContext) throws IOException {
		Method method = resourceInfo.getResourceMethod();

        if(method.isAnnotationPresent(PermitAll.class)) return;
        
        if(method.isAnnotationPresent(DenyAll.class)) {
        	requestContext.abortWith(Response.status(Response.Status.FORBIDDEN)
        			.entity("Access blocked for all users.").build());
        	return;
        }

        final List<String> authorization = requestContext.getHeaders().get(AUTHORIZATION_PROPERTY);
        if(authorization == null || authorization.isEmpty())
        {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
        			.entity("Page requires sending configured client application identification header.").build());
            return;
        }
        
        User user = AuthenticationManager.validateToken(authorization.get(0));
        if(user == null) {
        	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
        			.entity("Username/password invalid.").build());
            return;
        }
        
        requestContext.setProperty("loggedUser", user);
        
        if(method.isAnnotationPresent(RolesAllowed.class)) {
            RolesAllowed rolesAnnotation = method.getAnnotation(RolesAllowed.class);
            Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
              
            if(!isUserAllowed(user, rolesSet)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
            			.entity("You are not allowed to execute this operation.").build());
                return;
            }
        }        
	}
	
	private boolean isUserAllowed(User user, final Set<String> rolesSet) {		
		if(user == null || rolesSet.contains(user.getProfile().name())) {
			return false;
		}		
		return true;
	}

}
