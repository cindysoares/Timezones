package br.com.timezones.model;

public class Timezone {
	
	private String name;
	private String city;
	private Integer gmtDifference;
	
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
	
}
