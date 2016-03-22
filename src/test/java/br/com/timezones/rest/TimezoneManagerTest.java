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
    public void test_addTimezoneWithoutAuthorizationToken() {
		target.request()
        		.post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 1), MediaType.APPLICATION_JSON), Timezone.class);
    }
    
    @Test
    public void test_addTimezone() {
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
    public void test_addTimezoneSettingDifferentUserFromLogged() {
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
    public void test_addTimezoneWhenManagerUserIsLogged() {
		requestBuilder("manager@email.com", "1234")
        		.post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 3), MediaType.APPLICATION_JSON), Timezone.class);
    }
    
    @Test
    public void test_addTimezoneToANonExistentUser() {
    	Timezone responseMsg = requestBuilder().post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 999), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNotNull("Didn't add the timezone.", responseMsg);
        Assert.assertNotNull("Didn't set an id.", responseMsg.getId());
        Assert.assertEquals("AKST", responseMsg.getName());
        Assert.assertEquals("Fairbanks", responseMsg.getCity());
        Assert.assertEquals(-9, responseMsg.getGmtDifference());
        Assert.assertEquals(new Integer(1), responseMsg.getUserId());

    }

    @Test(expected=NotAuthorizedException.class)
    public void test_removeTimezoneWithoutAuthorizationToken() {
		target.path("/2").request().delete(Boolean.class);
    }

    @Test
    public void test_removeTimezone() {
		Boolean responseMsg = requestBuilder("/2").delete(Boolean.class);
        Assert.assertTrue("Didn't remove the timezone.", responseMsg);
    }

    @Test
    public void test_removeTimezoneFromAnotherUserWhenAdminUserIsLogged() {
		Boolean responseMsg = requestBuilder("/5", "admin@email.com", "4321").delete(Boolean.class);
        Assert.assertTrue("Didn't remove the timezone.", responseMsg);
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_removeTimezoneFromAnotherUserWhenRegularUserIsLogged() {
		requestBuilder("/6", "manager@email.com", "1234").delete(Boolean.class);
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_removeTimezoneWhenManagerUserIsLogged() {
		requestBuilder("/3", "manager@email.com", "1234").delete(Boolean.class);
    }

    @Test
    public void test_removeNonExistentTimezone() {
		Boolean responseMsg = requestBuilder("/9999").delete(Boolean.class);
        Assert.assertFalse("Removed a non-existent timezone.", responseMsg);
    }

    @Test(expected=NotAuthorizedException.class)
    public void test_updateTimezoneWithoutAuthorizationToken() {
		target.path("/1").request()
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 1), MediaType.APPLICATION_JSON), Timezone.class);
    }

    @Test
    public void test_updateTimezone() {
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
    public void test_updateTimezoneSettingDifferentUserFromLoggedWhenRegularUserLogged() {
		requestBuilder("/1")
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 4), MediaType.APPLICATION_JSON), Timezone.class);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_updateTimezoneWhenManagerUserLogged() {
		requestBuilder("/1")
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 4), MediaType.APPLICATION_JSON), Timezone.class);
    }

    @Test
    public void test_updateTimezoneSettingDifferentUserFromLoggedWhenAdminUserLogged() {
		Timezone responseMsg = requestBuilder("/1", "admin@email.com", "4321")
        		.put(Entity.entity(new Timezone("CST", "Mexico", -6, 4), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNotNull("Didn't update the timezone.", responseMsg);
        Assert.assertEquals(new Integer(1), responseMsg.getId());
        Assert.assertEquals("CST", responseMsg.getName());
        Assert.assertEquals("Mexico", responseMsg.getCity());
        Assert.assertEquals(-6, responseMsg.getGmtDifference());
        Assert.assertEquals(new Integer(1), responseMsg.getUserId());
    }    
    
    @Test
    public void test_updateANonExistentTimezone() {
        Timezone responseMsg = requestBuilder("/999")
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 1), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNull("Updated a non-existent timezone.", responseMsg);
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_findAllWithoutAuthorizationToken() {
		target.request().get(List.class);    	
    }

    @Test
    public void test_findAllWhenRegularUser() {
    	@SuppressWarnings("rawtypes")
		List responseMsg = requestBuilder().get(List.class);
    	Assert.assertNotNull(responseMsg);
    	Assert.assertEquals(4, responseMsg.size());
    }
    
    @Test(expected=NotAuthorizedException.class)
    public void test_findAllWhenManagerUser() {
    	requestBuilder("manager@email.com", "1234").get(List.class);
    }
    
    @Test
    public void test_findAllWhenAdminUser() {
    	@SuppressWarnings("rawtypes")
		List responseMsg = requestBuilder("admin@email.com", "4321")
    			.get(List.class);
    	Assert.assertNotNull(responseMsg);
    	Assert.assertTrue(responseMsg.size()>=5);
    }

}
