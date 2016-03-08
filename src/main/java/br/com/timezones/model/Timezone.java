package br.com.timezones.model;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Timezone {
	
	private String name;
	private String city;
	private TimeZone gmtDifference;
	private Calendar currentTime;
	
	public Timezone() {
	}
	
	public Timezone(String name, String city, Integer gmt) {
		this.name = name;
		this.city = city;
		this.gmtDifference = TimeZone.getTimeZone("GMT"+gmt);
		this.currentTime = new GregorianCalendar(gmtDifference);
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public TimeZone getGmtDifference() {
		return gmtDifference;
	}
	
	public Calendar getCurrentTime() {
		return currentTime;
	}
	
}
