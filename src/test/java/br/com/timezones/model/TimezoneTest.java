package br.com.timezones.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;


public class TimezoneTest {
	
	@Test
	public void testGetCurrentTime() {
		Timezone timezone = new Timezone("X", "Y", -8);
		
		Assert.assertEquals("Hour difference from GMT+0 different from expected.", -8,
				(new GregorianCalendar(TimeZone.getTimeZone("GMT+0")).get(Calendar.ZONE_OFFSET) + 
				timezone.getCurrentTime().get(Calendar.ZONE_OFFSET))/3600000);
		
		Assert.assertEquals("GMT-08:00", timezone.getGmtDifference().getID());
	}

}
