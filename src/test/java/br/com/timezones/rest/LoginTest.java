package br.com.timezones.rest;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.timezones.model.Profile;
import br.com.timezones.model.User;

public class LoginTest extends JerseyTest {

	HttpAuthenticationFeature authFilter;
	WebTarget target;
	
    @Override
    protected ResourceConfig configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        return new JerseyConfig();
    }
    
    @Before
    @Override
    public void setUp() throws Exception {
    	super.setUp();
    	authFilter = HttpAuthenticationFeature.basicBuilder().build();
    	target = target("/login").register(authFilter);
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
        target.queryParam("email", "any@email.com")
        		.request(MediaType.APPLICATION_JSON)
        		.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, "any@email.com")
        	    .property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, "xxxx")
        		.get(User.class);
    }

}
