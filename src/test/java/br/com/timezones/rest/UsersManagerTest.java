package br.com.timezones.rest;

import java.util.Set;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.Test;

import br.com.timezones.model.Profile;
import br.com.timezones.model.User;

public class UsersManagerTest extends RestTest {
	
	@Override
	protected String getBasePath() {
		return "/users";
	}
	
    @Override
    protected ResourceConfig configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        return new JerseyConfig();
    }
    
    @Test
    public void test_addRegularUserWithoutAuthorizationToken() {
		User responseMsg = target.path("/regular").request()
        		.post(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.ADMIN_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNotNull("Didn´t add the user.", responseMsg);
        Assert.assertNotNull("Didn´t set an id.", responseMsg.getId());
        Assert.assertEquals("Michael", responseMsg.getName());
        Assert.assertEquals("michael@email.com", responseMsg.getEmail());
        Assert.assertEquals(null, responseMsg.getPassword());
        Assert.assertEquals(null, responseMsg.getNewPassword());
        Assert.assertEquals(Profile.USER, responseMsg.getProfile());
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_addUserWithoutAuthorizationToken() {
		target.request()
        		.post(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.ADMIN_MANAGER), MediaType.APPLICATION_JSON), User.class);
    }

    @Test
    public void test_addUser() {
		User responseMsg = requestBuilder()
        		.post(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.ADMIN_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNotNull("Didn´t add the user.", responseMsg);
        Assert.assertNotNull("Didn´t set an id.", responseMsg.getId());
        Assert.assertEquals("Michael", responseMsg.getName());
        Assert.assertEquals("michael@email.com", responseMsg.getEmail());
        Assert.assertEquals(null, responseMsg.getPassword());
        Assert.assertEquals(null, responseMsg.getNewPassword());
        Assert.assertEquals(Profile.ADMIN_MANAGER, responseMsg.getProfile());
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_updateUserWithoutAuthorizationToken() {
    	target.path("/4").request()
        		.put(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
    }
    
    @Test
    public void test_updateUser() {
    	User responseMsg = requestBuilder("/4")
        		.put(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNotNull("Didn´t update the user.", responseMsg);
        Assert.assertEquals("Wrong id.", new Integer(4), responseMsg.getId());
        Assert.assertEquals("Michael", responseMsg.getName());
        Assert.assertEquals("michael@email.com", responseMsg.getEmail());
        Assert.assertNull(responseMsg.getPassword());
        Assert.assertEquals(Profile.USER_MANAGER, responseMsg.getProfile());
    }
    
    @Test
    public void test_updateNonExistentUser() {
    	User responseMsg = requestBuilder("/999")
        		.put(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNull("Updated a non-existent user.", responseMsg);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_removeUserWithoutAuthorizationToken() {
    	target.path("/4").request().delete(Boolean.class);        
    }
    
    @Test
    public void test_removeUser() {
    	boolean responseMsg = requestBuilder("/4").delete(Boolean.class);
        Assert.assertTrue("Didn´t remove the user.", responseMsg);
    }
    
    @Test
    public void test_removeNonExistentUser() {
    	boolean responseMsg = requestBuilder("/9999").delete(Boolean.class);
        Assert.assertFalse("Removed a non-existend user.", responseMsg);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_getAllWithoutAuthorizationToken() {
    	target.request().get(Set.class);
    }

    @Test
    public void test_getAll() {
    	Set<?> responseMsg = requestBuilder().get(Set.class);
        Assert.assertNotNull("Didn´t find all users.", responseMsg);
    }


}
