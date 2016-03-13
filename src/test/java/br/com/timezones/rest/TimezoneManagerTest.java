package br.com.timezones.rest;

import java.util.List;

import javax.ws.rs.ClientErrorException;
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
    
    @Test
    public void test_addTimezone() {
		Timezone responseMsg = requestBuilder()
        		.post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 1), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNotNull("Didn�t add the timezone.", responseMsg);
        Assert.assertNotNull("Didn�t set an id.", responseMsg.getId());
        Assert.assertEquals("AKST", responseMsg.getName());
        Assert.assertEquals("Fairbanks", responseMsg.getCity());
        Assert.assertEquals(-9, responseMsg.getGmtDifference());
    }
    
    @Test
    public void test_addTimezoneToANonExistentUser() {
    	expectedException.expect(ClientErrorException.class);
    	expectedException.expectMessage("User id doesn't exists: 999");

    	requestBuilder().post(Entity.entity(new Timezone("AKST", "Fairbanks", -9, 999), MediaType.APPLICATION_JSON), Timezone.class);        
    }

    @Test
    public void test_removeTimezone() {
		Boolean responseMsg = requestBuilder("/2").delete(Boolean.class);
        Assert.assertTrue("Didn�t remove the timezone.", responseMsg);
    }

    @Test
    public void test_removeNonExistentTimezone() {
		Boolean responseMsg = requestBuilder("/9999").delete(Boolean.class);
        Assert.assertFalse("Removed a non-existent timezone.", responseMsg);
    }

    @Test
    public void test_updateTimezone() {
		Timezone responseMsg = requestBuilder("/1")
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 1), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNotNull("Didn�t update the timezone.", responseMsg);
        Assert.assertEquals(new Integer(1), responseMsg.getId());
        Assert.assertEquals("VUT", responseMsg.getName());
        Assert.assertEquals("Port Vila", responseMsg.getCity());
        Assert.assertEquals(11, responseMsg.getGmtDifference());
    }
    
    @Test
    public void test_updateANonExistentTimezone() {
        Timezone responseMsg = requestBuilder("/999")
        		.put(Entity.entity(new Timezone("VUT", "Port Vila", 11, 1), MediaType.APPLICATION_JSON), Timezone.class);
        Assert.assertNull("Updated a non-existent timezone.", responseMsg);
    }
    
    @Test
    public void test_findAllWhenRegularUser() {
    	@SuppressWarnings("rawtypes")
		List responseMsg = requestBuilder("/1").get(List.class);
    	Assert.assertNotNull(responseMsg);
    	Assert.assertEquals(4, responseMsg.size());
    }
    /*
    @Test(expected=UnsupportedOperationException.class)
    public void test_findAllWhenManagerUser() {
    	WebTarget target = target();
    	target.path("/timezones/2")
    			.request(MediaType.APPLICATION_JSON).post(null, List.class);
    }*/
    
    @Test
    public void test_findAllWhenAdminUser() {
    	@SuppressWarnings("rawtypes")
		List responseMsg = requestBuilder("/3", "admin@email.com", "4321")
    			.get(List.class);
    	Assert.assertNotNull(responseMsg);
    	Assert.assertTrue(responseMsg.size()>=5);
    }

}
