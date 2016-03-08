package br.com.timezones.model;

import java.text.DecimalFormat;
import java.util.TimeZone;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Timezone {
	
	private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat("+#;-#");
	private static int counter = 0;
	
	private Integer id;
	private String name;
	private String city;
	private int gmtDifference;
	private TimeZone zoneInfo;
	
	public Timezone() {
	}
	
	public Timezone(String name, String city, Integer gmtDifference) {
		this.id = ++counter;
		this.name = name;
		this.city = city;
		setGmtDifference(gmtDifference);
	}

	public Integer getId() {
		return id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setGmtDifference(int gmtDifference) {
		if(gmtDifference>12 || gmtDifference<-12) throw new IllegalArgumentException("GMT difference must be between -12 and 12.");
		this.gmtDifference = gmtDifference;
		this.zoneInfo = TimeZone.getTimeZone("GMT" + DECIMAL_FORMAT.format(gmtDifference));
	}

	public int getGmtDifference() {
		return gmtDifference;
	}
	
	public TimeZone getZoneInfo() {
		return zoneInfo;
	}
	
}
