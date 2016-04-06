package br.com.timezones.rest;

import java.util.List;

import javax.ws.rs.NotAuthorizedException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import br.com.timezones.model.Timezone;

public class TimezoneManagerTest extends RestTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none(); 
	
    protected String getBasePath() {
    	return "/timezones";
    };
    
    @Test(expected=NotAuthorizedException.class)
    public void testAddTimezoneWithoutAuthorizationToken() {
		target.request()
        		.post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 1), MediaType.APPLICATION_JSON), Timezone.class);
    }
    
    @Test
    public void testAddTimezone() {
		Timezone responseMsg = requestBuilder()
        		.post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 1), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNotNull("Didn't add the timezone.", responseMsg);
        Assert.assertNotNull("Didn't set an id.", responseMsg.getId());
        Assert.assertEquals("AKST", responseMsg.getName());
        Assert.assertEquals("Fairbanks", responseMsg.getCity());
        Assert.assertEquals(-9, responseMsg.getGmtDifference());
        Assert.assertEquals(new Integer(1), responseMsg.getUserId());
        Assert.assertNotNull("Didn't generate an id.", responseMsg.getId());
        
        requestBuilder("/"+responseMsg.getId()).delete();
    }
    
    @Test
    public void testAddTimezoneSettingDifferentUserFromLogged() {
		Timezone responseMsg = requestBuilder()
        		.post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 3), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNotNull("Didn't add the timezone.", responseMsg);
        Assert.assertNotNull("Didn't set an id.", responseMsg.getId());
        Assert.assertEquals("AKST", responseMsg.getName());
        Assert.assertEquals("Fairbanks", responseMsg.getCity());
        Assert.assertEquals(-9, responseMsg.getGmtDifference());
        Assert.assertEquals(new Integer(1), responseMsg.getUserId());
        Assert.assertNotNull("Didn't generate an id.", responseMsg.getId());
        
        requestBuilder("/"+responseMsg.getId()).delete();
    }

    @Test(expected=NotAuthorizedException.class)
    public void testAddTimezoneWhenManagerUserIsLogged() {
		requestBuilder(MANAGER_USER_EMAIL, MANAGER_USER_PASSWORD)
        		.post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 3), MediaType.APPLICATION_JSON), Timezone.class);
    }
    
    @Test
    public void testAddTimezoneToANonExistentUser() {
    	Timezone responseMsg = requestBuilder().post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 999), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNotNull("Didn't add the timezone.", responseMsg);
        Assert.assertNotNull("Didn't set an id.", responseMsg.getId());
        Assert.assertEquals("AKST", responseMsg.getName());
        Assert.assertEquals("Fairbanks", responseMsg.getCity());
        Assert.assertEquals(-9, responseMsg.getGmtDifference());
        Assert.assertEquals(new Integer(1), responseMsg.getUserId());

    }

    @Test(expected=NotAuthorizedException.class)
    public void testRemoveTimezoneWithoutAuthorizationToken() {
		target.path("/2").request().delete(Boolean.class);
    }

    @Test
    public void testRemoveTimezone() {
		Boolean responseMsg = requestBuilder("/2").delete(Boolean.class);
        Assert.assertTrue("Didn't remove the timezone.", responseMsg);
    }

    @Test
    public void testRemoveTimezoneFromAnotherUserWhenAdminUserIsLogged() {
		Boolean responseMsg = requestBuilder("/5", ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD).delete(Boolean.class);
        Assert.assertTrue("Didn't remove the timezone.", responseMsg);
    }

    @Test(expected=NotAuthorizedException.class)
    public void testRemoveTimezoneFromAnotherUserWhenRegularUserIsLogged() {
		requestBuilder("/6", REGULAR_USER_EMAIL, REGULAR_USER_PASSWORD).delete(Boolean.class);
    }

    @Test(expected=NotAuthorizedException.class)
    public void testRemoveTimezoneWhenManagerUserIsLogged() {
		requestBuilder("/3", MANAGER_USER_EMAIL, MANAGER_USER_PASSWORD).delete(Boolean.class);
    }

    @Test
    public void testRemoveNonExistentTimezone() {
		Boolean responseMsg = requestBuilder("/9999").delete(Boolean.class);
        Assert.assertFalse("Removed a non-existent timezone.", responseMsg);
    }

    @Test(expected=NotAuthorizedException.class)
    public void testUpdateTimezoneWithoutAuthorizationToken() {
		target.path("/1").request()
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 1), MediaType.APPLICATION_JSON), Timezone.class);
    }

    @Test
    public void testUpdateTimezone() {
		Timezone responseMsg = requestBuilder("/1")
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 1), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNotNull("Didn't update the timezone.", responseMsg);
        Assert.assertEquals(new Integer(1), responseMsg.getId());
        Assert.assertEquals("VUT", responseMsg.getName());
        Assert.assertEquals("Port Vila", responseMsg.getCity());
        Assert.assertEquals(11, responseMsg.getGmtDifference());
        Assert.assertEquals(new Integer(1), responseMsg.getUserId());
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void testUpdateTimezoneSettingDifferentUserFromLoggedWhenRegularUserLogged() {
		requestBuilder("/1")
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 4), MediaType.APPLICATION_JSON), Timezone.class);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void testUpdateTimezoneWhenManagerUserLogged() {
		requestBuilder("/1")
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 4), MediaType.APPLICATION_JSON), Timezone.class);
    }

    @Test
    public void testUpdateTimezoneSettingDifferentUserFromLoggedWhenAdminUserLogged() {
		Timezone responseMsg = requestBuilder("/1", ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD)
        		.put(Entity.entity(new Timezone("CST", "Mexico", -6, 4), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNotNull("Didn't update the timezone.", responseMsg);
        Assert.assertEquals(new Integer(1), responseMsg.getId());
        Assert.assertEquals("CST", responseMsg.getName());
        Assert.assertEquals("Mexico", responseMsg.getCity());
        Assert.assertEquals(-6, responseMsg.getGmtDifference());
        Assert.assertEquals(new Integer(1), responseMsg.getUserId());
    }    
    
    @Test
    public void testUpdateANonExistentTimezone() {
        Timezone responseMsg = requestBuilder("/999")
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 1), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNull("Updated a non-existent timezone.", responseMsg);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void testFindAllWithoutAuthorizationToken() {
		target.request().get(List.class);    	
    }

    @Test
    public void testFindAllWhenRegularUser() {
    	@SuppressWarnings("rawtypes")
		List responseMsg = requestBuilder().get(List.class);
    	Assert.assertNotNull(responseMsg);
    	Assert.assertEquals(4, responseMsg.size());
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void testFindAllWhenManagerUser() {
    	requestBuilder(MANAGER_USER_EMAIL, MANAGER_USER_PASSWORD).get(List.class);
    }
    
    @Test
    public void testFindAllWhenAdminUser() {
    	@SuppressWarnings("rawtypes")
		List responseMsg = requestBuilder(ADMIN_USER_EMAIL, ADMIN_USER_PASSWORD)
    			.get(List.class);
    	Assert.assertNotNull(responseMsg);
    	Assert.assertTrue(responseMsg.size()>=5);
    }

}
