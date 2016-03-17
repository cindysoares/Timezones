package br.com.timezones.rest;

import br.com.timezones.model.Timezone;
import java.util.List;

import br.com.timezones.model.User;

public class TimezoneManagerRegularUserStrategy extends TimezoneManagerStrategy {	
	
	public TimezoneManagerRegularUserStrategy(User user) {
		super(user);
	}
	
	public List<Timezone> findAll() {
		return timezoneDAO.findByUser(user.getId());
	}
	
	@Override
	public boolean removeTimezone(Integer timezoneId) {
		Timezone timezone = timezoneDAO.find(timezoneId);
		if(timezone!=null && !user.getId().equals(timezone.getUserId())) {
			throw new UnsupportedOperationException();
		}
		return super.removeTimezone(timezoneId);
	}
	
}
