package br.com.timezones.dao;

import java.util.HashSet;
import java.util.Set;

import br.com.timezones.model.Profile;
import br.com.timezones.model.Timezone;
import br.com.timezones.model.User;

public class MemoryUserDAO implements UserDAO {
	
	private static Set<User> users = new HashSet<User>();
	
	static {
		User regular_user = new User("Cindy Soares", "cindy@email.com", "senha", Profile.USER);
		regular_user.addTimezone(new Timezone("PST", "Los Angeles", -8));
		regular_user.addTimezone(new Timezone("PET", "Lima", -5));
		regular_user.addTimezone(new Timezone("BRT", "Rio de Janeiro", -3));
		regular_user.addTimezone(new Timezone("GMT", "London", 0));
		regular_user.addTimezone(new Timezone("MSK", "Moskow", +3));
		regular_user.addTimezone(new Timezone("JST", "Tokio", +9));
		
		users.add(regular_user);
		users.add(new User("User manager", "manager@email.com", "1234", Profile.USER_MANAGER));
		users.add(new User("User admin", "admin@email.com", "4321", Profile.ADMIN_MANAGER));
	}
	
	public User find(Integer userId) {
		return (User) users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
	}
	
	public User find(String email) {
		return users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
	}
	
	public User save(User user) {
		boolean saved = users.add(user);
		if(saved) return user;
		return null;
	}
	
	public User update(User user) {
		boolean removed = users.remove(user);
		if(removed) {
			boolean saved = users.add(user);
			if(saved) return user;
		}
		return null;
	}
	
	public boolean remove(Integer userId) {
		return users.remove(find(userId));
	}
	
	@Override
	public Set<User> findAll() {
		return users;
	}

}
