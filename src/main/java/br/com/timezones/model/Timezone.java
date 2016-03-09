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
	
	private Integer id;
	private String name;
	private String city;
	private Integer gmtDifference;
	private TimeZone zoneInfo;
	
	private Integer userId;
	
	public Timezone() {
	}
	
	public Timezone(Integer id, String name, String city, Integer gmtDifference, Integer userId) {
		this.id = id;
		this.name = name;
		this.city = city;
		setGmtDifference(gmtDifference);
		this.userId = userId;
	}
	
	public Timezone(String name, String city, Integer gmtDifference, Integer userId) {
		this(null, name, city, gmtDifference, userId);
	}

	public void setId(Integer id) {
		this.id = id;
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
	
	public void setGmtDifference(Integer gmtDifference) {
		if(gmtDifference ==null || gmtDifference>12 || gmtDifference<-12) throw new IllegalArgumentException("GMT difference must be between -12 and 12.");
		this.gmtDifference = gmtDifference;
		this.zoneInfo = TimeZone.getTimeZone("GMT" + DECIMAL_FORMAT.format(gmtDifference));
	}

	public int getGmtDifference() {
		return gmtDifference;
	}
	
	public TimeZone getZoneInfo() {
		return zoneInfo;
	}
	
	public Integer getUserId() {
		return userId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Timezone other = (Timezone) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}	
	
}
