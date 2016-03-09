package br.com.timezones.dao;

public class DAOFactory {
	
	public static UserDAO getUserDAO() {
		return new MemoryUserDAO();
	}

	public static TimezoneDAO getTimezoneDAO() {
		return new MemoryTimezoneDAO();
	}

}
