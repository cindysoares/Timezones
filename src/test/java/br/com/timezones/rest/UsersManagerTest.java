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
        Assert.assertNotNull("Didn't add the user.", responseMsg);
        Assert.assertNotNull("Didn't set an id.", responseMsg.getId());
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
    public void test_addUserWhenAdminUserLogged() {
		User responseMsg = requestBuilder(ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD)
        		.post(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.ADMIN_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNotNull("Didn't add the user.", responseMsg);
        Assert.assertNotNull("Didn't set an id.", responseMsg.getId());
        Assert.assertEquals("Michael", responseMsg.getName());
        Assert.assertEquals("michael@email.com", responseMsg.getEmail());
        Assert.assertEquals(null, responseMsg.getPassword());
        Assert.assertEquals(null, responseMsg.getNewPassword());
        Assert.assertEquals(Profile.ADMIN_MANAGER, responseMsg.getProfile());
    }
    
    @Test
    public void test_addUserWhenManagerUserLogged() {
		User responseMsg = requestBuilder(MANAGER_USER_EMAIL, MANAGER_USER_PASSWORD)
        		.post(Entity.entity(new User("Paul", "paul@email.com", "pass", Profile.ADMIN_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNotNull("Didn't add the user.", responseMsg);
        Assert.assertNotNull("Didn't set an id.", responseMsg.getId());
        Assert.assertEquals("Paul", responseMsg.getName());
        Assert.assertEquals("paul@email.com", responseMsg.getEmail());
        Assert.assertEquals(null, responseMsg.getPassword());
        Assert.assertEquals(null, responseMsg.getNewPassword());
        Assert.assertEquals(Profile.ADMIN_MANAGER, responseMsg.getProfile());
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_addUserWhenRegularUserLogged() {
		requestBuilder().post(Entity.entity(new User("Paul", "paul@email.com", "pass", Profile.ADMIN_MANAGER), MediaType.APPLICATION_JSON), User.class);
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_updateUserWithoutAuthorizationToken() {
    	target.path("/4").request()
        		.put(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
    }
    
    @Test
    public void test_updateUserWhenAdminUserLogged() {
    	User responseMsg = requestBuilder("/4", ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD)
        		.put(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNotNull("Didn't update the user.", responseMsg);
        Assert.assertEquals("Wrong id.", new Integer(4), responseMsg.getId());
        Assert.assertEquals("Michael", responseMsg.getName());
        Assert.assertEquals("michael@email.com", responseMsg.getEmail());
        Assert.assertNull(responseMsg.getPassword());
        Assert.assertEquals(Profile.USER_MANAGER, responseMsg.getProfile());
    }
    
    @Test
    public void test_updateUserWhenManagerUserLogged() {
    	User responseMsg = requestBuilder("/4", MANAGER_USER_EMAIL, MANAGER_USER_PASSWORD)
        		.put(Entity.entity(new User("Paul", "paul@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNotNull("Didn't update the user.", responseMsg);
        Assert.assertEquals("Wrong id.", new Integer(4), responseMsg.getId());
        Assert.assertEquals("Paul", responseMsg.getName());
        Assert.assertEquals("paul@email.com", responseMsg.getEmail());
        Assert.assertNull(responseMsg.getPassword());
        Assert.assertEquals(Profile.USER_MANAGER, responseMsg.getProfile());
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_updateUserWhenRegularUserLogged() {
    	requestBuilder("/4")
        		.put(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
    }

    @Test
    public void test_updateNonExistentUser() {
    	User responseMsg = requestBuilder("/999", ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD)
        		.put(Entity.entity(new User("Michael", "michael@email.com", "pass", Profile.USER_MANAGER), MediaType.APPLICATION_JSON), User.class);
        Assert.assertNull("Updated a non-existent user.", responseMsg);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_removeUserWithoutAuthorizationToken() {
    	target.path("/4").request().delete(Boolean.class);
    }
    
    @Test
    public void test_removeUserWhenAdminUserLogged() {
    	boolean responseMsg = requestBuilder("/5", ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD).delete(Boolean.class);
        Assert.assertTrue("Didn't remove the user.", responseMsg);
    }
    
    @Test
    public void test_removeUserWhenManagerUserLogged() {
    	boolean responseMsg = requestBuilder("/6", MANAGER_USER_EMAIL, MANAGER_USER_PASSWORD).delete(Boolean.class);
        Assert.assertTrue("Didn't remove the user.", responseMsg);
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_removeUserWhenRegularUserLogged() {
    	requestBuilder("/4").delete(Boolean.class);
    }

    @Test
    public void test_removeNonExistentUser() {
    	boolean responseMsg = requestBuilder("/9999", ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD).delete(Boolean.class);
        Assert.assertFalse("Removed a non-existend user.", responseMsg);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_getAllWithoutAuthorizationToken() {
    	target.request().get(Set.class);
    }

    @Test
    public void test_getAllWhenAdminUserLogged() {
    	Set<?> responseMsg = requestBuilder(ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD).get(Set.class);
        Assert.assertNotNull("Didn't find all users.", responseMsg);
    }

    @Test
    public void test_getAllWhenManagerUserLogged() {
    	Set<?> responseMsg = requestBuilder(MANAGER_USER_EMAIL, MANAGER_USER_PASSWORD).get(Set.class);
        Assert.assertNotNull("Didn't find all users.", responseMsg);
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_getAllWhenRegularUserLogged() {
    	requestBuilder().get(Set.class);
    }

}
