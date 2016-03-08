package br.com.timezones.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.junit.Assert;
import org.junit.Test;


public class TimezoneTest {
	
	@Test
	public void testGetGmtDifferenceWhenGMTMinus8() {
		Timezone timezone = new Timezone("X", "Y", -8);		
		Assert.assertEquals("GMT-08:00", timezone.getGmtDifference().getID());
	}
	
	@Test
	public void testGetGmtDifferenceWhenGMTPlus2() {
		Timezone timezone = new Timezone("X", "Y", 2);
		Assert.assertEquals("GMT+02:00", timezone.getGmtDifference().getID());
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNewTimezoneWhenGMTPlus50() {
		new Timezone("X", "Y", 50);
	}

}
