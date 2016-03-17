package br.com.timezones.rest;

import java.util.List;

import br.com.timezones.dao.DAOFactory;
import br.com.timezones.dao.TimezoneDAO;
import br.com.timezones.model.Timezone;
import br.com.timezones.model.User;

public abstract class TimezoneManagerStrategy {
	
	protected TimezoneDAO timezoneDAO = DAOFactory.getTimezoneDAO();
	protected User user;

	public abstract List<Timezone> findAll();
	
	public TimezoneManagerStrategy(User user) {
		this.user = user;
	}

	Timezone addTimezone(Timezone timezone) {
		timezone.setUserId(user.getId());
		return timezoneDAO.save(timezone);
	}

	public boolean removeTimezone(Integer timezoneId) {
		return timezoneDAO.remove(timezoneId);
	}

}
