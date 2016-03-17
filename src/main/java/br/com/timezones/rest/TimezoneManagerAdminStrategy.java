package br.com.timezones.rest;

import java.util.List;

import br.com.timezones.model.Timezone;
import br.com.timezones.model.User;

public class TimezoneManagerAdminStrategy extends TimezoneManagerStrategy {
	
	public TimezoneManagerAdminStrategy(User user) {
		super(user);
	}
	
	@Override
	public List<Timezone> findAll() {
		return timezoneDAO.findAll();
	}
	
}
