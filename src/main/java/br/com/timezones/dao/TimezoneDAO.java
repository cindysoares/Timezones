package br.com.timezones.dao;

import java.util.List;

import br.com.timezones.model.Timezone;

public interface TimezoneDAO {
	
	public List<Timezone> findAll();
	public List<Timezone> findByUser(Integer userId);
	public Timezone save(Timezone value);
	public Timezone update(Timezone value);
	public Timezone find(Integer timezoneId);
	boolean remove(Integer timezoneId);

}
