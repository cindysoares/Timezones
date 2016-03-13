package br.com.timezones.rest;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.com.timezones.model.Profile;
import br.com.timezones.model.User;

public class LoginTest extends RestTest {
	
	public ExpectedException expectedException = ExpectedException.none();
    
    @Override
    protected String getBasePath() {
    	return "/login";
    }
    
    @Test
    public void test_loginUserSuccess() {
        User responseMsg = target.queryParam("email", "cindy@email.com")
        		.request(MediaType.APPLICATION_JSON)
        		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, "cindy@email.com")
        	    .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, "senha")
        	    .get(User.class);
        Assert.assertNotNull("Didn´t find any user.", responseMsg);
        Assert.assertEquals("Wrong user.", "Cindy Soares", responseMsg.getName());
        Assert.assertEquals("Wrong profile.", Profile.USER, responseMsg.getProfile());
        Assert.assertNull("Shouldn´t serialize password.", responseMsg.getPassword());
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_loginUserWhenPasswordIsWrong() {
        target.queryParam("email", "cindy@email.com")
        		.request(MediaType.APPLICATION_JSON)
        		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, "cindy@email.com")
        	    .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, "xxxx")
        		.get(User.class);
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_loginUserWhenUserDoesntExists() {
    	expectedException.expect(NotAuthorizedException.class);
        target.queryParam("email", "any@email.com")
        		.request(MediaType.APPLICATION_JSON)
        		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, "any@email.com")
        	    .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, "xxxx")
        		.get(User.class);
    }

}
