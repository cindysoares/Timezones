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
        	requestContext.abortWith(Response.status(Response.Status.FORBIDDEN).build());
        	return;
        }

        final List<String> authorization = requestContext.getHeaders().get(AUTHORIZATION_PROPERTY);
        if(authorization == null || authorization.isEmpty())
        {
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        
        User user = AuthenticationManager.validateToken(authorization.get(0), requestContext);
        if(user == null) {
        	requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
            return;
        }
        
        Class<?> resourceClass = resourceInfo.getResourceClass();
        RolesAllowed rolesAnnotation = null;
        if(method.isAnnotationPresent(RolesAllowed.class)) {
        	rolesAnnotation = method.getAnnotation(RolesAllowed.class);
        } else if(resourceClass.isAnnotationPresent(RolesAllowed.class)) {
        	rolesAnnotation = resourceClass.getAnnotation(RolesAllowed.class);
        }
        	
        if(rolesAnnotation!=null) {
            Set<String> rolesSet = new HashSet<String>(Arrays.asList(rolesAnnotation.value()));
              
            if(!isUserAllowed(user, rolesSet)) {
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).build());
                return;
            }
        }        
	}
	
	private boolean isUserAllowed(User user, final Set<String> rolesSet) {		
		return user != null && rolesSet.contains(user.getProfile().name());
	}

}
