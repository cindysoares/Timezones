package br.com.timezones.model;

import java.util.Calendar;
import java.util.Date;
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
	private Integer gmtDifference;
	
	public Timezone() {
	}
	
	public Timezone(String name, String city, Integer gmt) {
		this.name = name;
		this.city = city;
		this.gmtDifference = gmt;
	}

	public String getName() {
		return name;
	}

	public String getCity() {
		return city;
	}

	public Integer getGmtDifference() {
		return gmtDifference;
	}
	
	public GregorianCalendar getCurrentTime() {
		return new GregorianCalendar(TimeZone.getTimeZone("GMT"+gmtDifference));
	}
	
}
