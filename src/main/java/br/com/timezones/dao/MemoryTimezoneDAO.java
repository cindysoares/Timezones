package br.com.timezones.dao;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import br.com.timezones.model.Timezone;

public class MemoryTimezoneDAO implements TimezoneDAO {
	
	private static final List<Timezone> timezones = new LinkedList<Timezone>();
	private static int counter = 0;
	
	static {
		add(new Timezone("PST", "Los Angeles", -8, 1));
		add(new Timezone("PET", "Lima", -5, 1));
		add(new Timezone("BRT", "Rio de Janeiro", -3, 1));
		add(new Timezone("GMT", "London", 0, 1));
		add(new Timezone("MSK", "Moskow", +3, 3));
		add(new Timezone("JST", "Tokio", +9, 3));
	}

	protected MemoryTimezoneDAO() {
	}

	@Override
	public List<Timezone> findAll() {
		return timezones;
	}

	@Override
	public List<Timezone> findByUser(Integer userId) {
		return timezones.stream().filter(t -> t.getUserId().equals(userId)).collect(Collectors.toList());
	}

	@Override
	public Timezone save(Timezone value) {
		return add(value);
	}
	
	private static Timezone add(Timezone newValue) {
		newValue.setId(++counter);
		boolean wasAdded = timezones.add(newValue);		
		return wasAdded? newValue:null;
	}
	
	@Override
	public Timezone update(Timezone newValues) {
		Timezone timezoneToUpdate = find(newValues.getId());
		if(timezoneToUpdate != null) {
			timezoneToUpdate.setName(newValues.getName());
			timezoneToUpdate.setCity(newValues.getCity());
			timezoneToUpdate.setGmtDifference(newValues.getGmtDifference());
		}
		return timezoneToUpdate;
	}

	@Override
	public Timezone find(Integer timezoneId) {
		return timezones.stream().filter(t -> t.getId().equals(timezoneId)).findFirst().orElse(null);
	}
	
	@Override
	public boolean remove(Integer timezoneId) {
		return timezones.remove(find(timezoneId));
	}
	
}
