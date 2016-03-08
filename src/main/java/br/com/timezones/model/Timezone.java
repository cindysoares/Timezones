package br.com.timezones.model;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Timezone {
	
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("+#;-#");
	
	private String name;
	private String city;
	private TimeZone gmtDifference;
	
	public Timezone() {
	}
	
	public Timezone(String name, String city, Integer gmt) {
		if(gmt>12 || gmt<-12) throw new IllegalArgumentException("GMT difference must be between -12 and 12.");
		this.name = name;
		this.city = city;
		this.gmtDifference = TimeZone.getTimeZone("GMT" + DECIMAL_FORMAT.format(gmt));
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
	
}
