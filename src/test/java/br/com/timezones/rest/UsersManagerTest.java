package br.com.timezones.rest;

import java.util.Set;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;

import br.com.timezones.model.Profile;
import br.com.timezones.model.User;

public class UsersManagerTest extends JerseyTest {
	
    @Override
    protected ResourceConfig configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        return new JerseyConfig();
    }
    
    @Test
    public void test_addUser() {
    	WebTarget target = target();
		User responseMsg = target.path("/users")
        		.request(MediaType.APPLICATION_JSON)
        		.post(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNotNull("Didn�t add the user.", responseMsg);
        Assert.assertNotNull("Didn�t set an id.", responseMsg.getId());
        Assert.assertEquals("Michael", responseMsg.getName());
        Assert.assertEquals("michael@email.com", responseMsg.getEmail());
        Assert.assertEquals(null, responseMsg.getPassword());
        Assert.assertEquals(null, responseMsg.getNewPassword());
        Assert.assertEquals(Profile.USER, responseMsg.getProfile());
    }
    
    @Test
    public void test_updateUser() {
    	WebTarget target = target();
    	User responseMsg = target.path("/users/4")
        		.request(MediaType.APPLICATION_JSON)
        		.put(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNotNull("Didn�t update the user.", responseMsg);
        Assert.assertEquals("Wrong id.", new Integer(4), responseMsg.getId());
        Assert.assertEquals("Michael", responseMsg.getName());
        Assert.assertEquals("michael@email.com", responseMsg.getEmail());
        Assert.assertNull(responseMsg.getPassword());
        Assert.assertEquals(Profile.USER_MANAGER, responseMsg.getProfile());
    }
    
    @Test
    public void test_updateNonExistentUser() {
    	WebTarget target = target();
    	User responseMsg = target.path("/users/999")
        		.request(MediaType.APPLICATION_JSON)
        		.put(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNull("Updated a non-existent user.", responseMsg);
    }
    
    @Test
    public void test_removeUser() {
    	WebTarget target = target();
    	boolean responseMsg = target.path("/users/4")
        		.request(MediaType.APPLICATION_JSON).delete(Boolean.class);
        Assert.assertTrue("Didn�t remove the user.", responseMsg);
    }
    
    @Test
    public void test_removeNonExistentUser() {
    	WebTarget target = target();
    	boolean responseMsg = target.path("/users/9999")
        		.request(MediaType.APPLICATION_JSON).delete(Boolean.class);
        Assert.assertFalse("Removed a non-existend user.", responseMsg);
    }
    
    @Test
    public void test_getAll() {
    	WebTarget target = target();
    	Set<?> responseMsg = target.path("/users")
        		.request(MediaType.APPLICATION_JSON).get(Set.class);
        Assert.assertNotNull("Didn�t find all users.", responseMsg);
    }


}
