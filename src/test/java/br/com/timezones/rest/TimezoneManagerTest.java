package br.com.timezones.rest;

import java.util.List;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import br.com.timezones.model.Timezone;

public class TimezoneManagerTest  extends JerseyTest {
	
    @Override
    protected ResourceConfig configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        return new JerseyConfig();
    }
    
    @BeforeClass
    public static void createScenario() {
    }
    
    @Test
    public void test_addTimezone() {
        WebTarget target = target();
		Timezone responseMsg = target.path("/timezones/add/1")
        		.queryParam("name", "AKST")
        		.queryParam("city", "Fairbanks")
        		.queryParam("gmtDifference", -9)
        		.request(MediaType.APPLICATION_JSON).post(null, Timezone.class);
        Assert.assertNotNull("Didn´t add the timezone.", responseMsg);
        Assert.assertNotNull("Didn´t set an id.", responseMsg.getId());
        Assert.assertEquals("AKST", responseMsg.getName());
        Assert.assertEquals("Fairbanks", responseMsg.getCity());
        Assert.assertEquals(-9, responseMsg.getGmtDifference());
    }
    
    @Test
    public void test_addTimezoneToANonExistentUser() {
        WebTarget target = target();
        Timezone responseMsg = target.path("/timezones/add/999")
        		.request(MediaType.APPLICATION_JSON).post(null, Timezone.class);
        Assert.assertNull("Added the timezone to a non-existent .", responseMsg);
    }

    @Test
    public void test_removeTimezone() {
        WebTarget target = target();
		Boolean responseMsg = target.path("/timezones/remove/1/2")
        		.request(MediaType.APPLICATION_JSON).delete(Boolean.class);
        Assert.assertTrue("Didn´t remove the timezone.", responseMsg);
    }

    @Test
    public void test_removeNonExistentTimezone() {
        WebTarget target = target();
		Boolean responseMsg = target.path("/timezones/remove/1/9999")
        		.request(MediaType.APPLICATION_JSON).delete(Boolean.class);
        Assert.assertFalse("Removed a non-existent timezone.", responseMsg);
    }

    @Test
    public void test_updateTimezone() {
        WebTarget target = target();
		Timezone responseMsg = target.path("/timezones/update/1/1")
        		.queryParam("name", "VUT")
        		.queryParam("city", "Port Vila")
        		.queryParam("gmtDifference", 11)
        		.request(MediaType.APPLICATION_JSON).post(null, Timezone.class);
        Assert.assertNotNull("Didn´t update the timezone.", responseMsg);
        Assert.assertEquals(new Integer(1), responseMsg.getId());
        Assert.assertEquals("VUT", responseMsg.getName());
        Assert.assertEquals("Port Vila", responseMsg.getCity());
        Assert.assertEquals(11, responseMsg.getGmtDifference());
    }
    
    @Test
    public void test_updateTimezoneToANonExistentUser() {
        WebTarget target = target();
        Timezone responseMsg = target.path("/timezones/update/1/999")
        		.queryParam("name", "VUT")
        		.queryParam("city", "Port Vila")
        		.queryParam("gmtDifference", 11)
        		.request(MediaType.APPLICATION_JSON).post(null, Timezone.class);
        Assert.assertNull("Updated a non-existent timezone.", responseMsg);
    }
    
    @Test
    public void test_findAllWhenRegularUser() {
    	WebTarget target = target();
    	@SuppressWarnings("rawtypes")
		List responseMsg = target.path("/timezones/1")
    			.request(MediaType.APPLICATION_JSON).post(null, List.class);
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
    	WebTarget target = target();
    	@SuppressWarnings("rawtypes")
		List responseMsg = target.path("/timezones/3")
    			.request(MediaType.APPLICATION_JSON).post(null, List.class);
    	Assert.assertNotNull(responseMsg);
    	Assert.assertTrue(responseMsg.size()>=5);
    }

}
