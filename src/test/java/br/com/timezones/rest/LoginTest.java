package br.com.timezones.rest;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;

import br.com.timezones.authentication.Credentials;
import br.com.timezones.model.User;

public class LoginTest extends RestTest {
	
    @Override
    protected String getBasePath() {
    	return "/login";
    }
    
    @Test
    public void test_loginUserSuccess() {
        String responseMsg = target
        		.request(MediaType.APPLICATION_JSON)
        	    .post(Entity.entity(new Credentials("cindy@email.com", "senha"), MediaType.APPLICATION_JSON), String.class);
        Assert.assertNotNull("Didn´t generate any token.", responseMsg);
        Assert.assertTrue("Invalid token.", responseMsg.startsWith("Basic "));
        
        User loggedUser = requestBuilder().get(User.class);
        Assert.assertNotNull("Logged user shouldn't be null", loggedUser);
        Assert.assertEquals("Wrong name.", "Cindy Soares", loggedUser.getName());
        Assert.assertEquals("Wrong email.", "cindy@email.com", loggedUser.getEmail());
        Assert.assertNull("Shouldn't serialize password.", loggedUser.getPassword());
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_loginAuthenticationWhenPasswordIsWrong() {
    	String responseMsg = target.request(MediaType.APPLICATION_JSON)
    			.post(Entity.entity(new Credentials("cindy@email.com", "xxxx"), MediaType.APPLICATION_JSON), String.class);
    	Assert.assertNull("Shouldn't generate any token.", responseMsg);
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_loginAuthenticationWhenUserDoesntExists() {
    	String responseMsg = target.request(MediaType.APPLICATION_JSON)
    			.post(Entity.entity(new Credentials("any@email.com", "xxxx"), MediaType.APPLICATION_JSON), String.class);
    	Assert.assertNull("Shouldn't generate any token.", responseMsg);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_loginUserWithoutCredentials() {
    	User user = target.request(MediaType.APPLICATION_JSON)
    			.get(User.class);
    	Assert.assertNull("Shouldn't find any user.", user);
    }


}
