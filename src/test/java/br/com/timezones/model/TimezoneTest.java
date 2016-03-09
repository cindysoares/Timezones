package br.com.timezones.model;

import org.junit.Assert;
import org.junit.Test;


public class TimezoneTest {
	
	@Test
	public void testGetZoneInfoWhenGMTMinus8() {
		Timezone timezone = new Timezone("X", "Y", -8, 1);		
		Assert.assertEquals("GMT-08:00", timezone.getZoneInfo().getID());
	}
	
	@Test
	public void testGetZoneInfoWhenGMTPlus2() {
		Timezone timezone = new Timezone("X", "Y", 2, 1);
		Assert.assertEquals("GMT+02:00", timezone.getZoneInfo().getID());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNewTimezoneWhenGMTPlus50() {
		new Timezone("X", "Y", 50, 1);
	}

}
