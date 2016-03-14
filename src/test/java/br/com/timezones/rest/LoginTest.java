package br.com.timezones.rest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Test;

import br.com.timezones.model.Profile;
import br.com.timezones.model.User;

public class LoginTest extends RestTest {
	
    @Override
    protected String getBasePath() {
    	return "/login";
    }
    
    @Test
    public void test_loginUserSuccess() {
    	Form loginForm = new Form();
    	loginForm.param("email", "cindy@email.com");
    	loginForm.param("password", "senha");
        User responseMsg = target
        		.request(MediaType.APPLICATION_JSON)
        	    .post(Entity.entity(loginForm, MediaType.APPLICATION_FORM_URLENCODED), User.class);
        Assert.assertNotNull("Didn´t find any user.", responseMsg);
        Assert.assertEquals("Wrong user.", "Cindy Soares", responseMsg.getName());
        Assert.assertEquals("Wrong profile.", Profile.USER, responseMsg.getProfile());
        Assert.assertNull("Shouldn´t serialize password.", responseMsg.getPassword());
    }
    
    @Test
    public void test_loginUserWhenPasswordIsWrong() {
    	Form loginForm = new Form();
    	loginForm.param("email", "cindy@email.com");
    	loginForm.param("password", "xxxx");
        
    	User responseMsg = target.request(MediaType.APPLICATION_JSON)
        		.post(Entity.entity(loginForm, MediaType.APPLICATION_FORM_URLENCODED), User.class);
    	Assert.assertNull("Shouldn't find any user.", responseMsg);
    }

    @Test
    public void test_loginUserWhenUserDoesntExists() {
    	Form loginForm = new Form();
    	loginForm.param("email", "any@gmail.com");
    	loginForm.param("password", "xxxx");

    	User responseMsg = target.request(MediaType.APPLICATION_JSON)
        		.post(Entity.entity(loginForm, MediaType.APPLICATION_FORM_URLENCODED), User.class);
    	Assert.assertNull("Shouldn't find any user.", responseMsg);
    }

}
