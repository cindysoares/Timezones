package br.com.timezones.dao;

import java.util.HashSet;
import java.util.Set;

import br.com.timezones.model.Profile;
import br.com.timezones.model.User;

public class MemoryUserDAO implements UserDAO {
	
	private static Set<User> users = new HashSet<User>();
	private static int counter;
	
	static {		
		add(new User("Cindy Soares", "cindy@email.com", "senha", Profile.USER));
		add(new User("User manager", "manager@email.com", "1234", Profile.USER_MANAGER));
		add(new User("User admin", "admin@email.com", "4321", Profile.ADMIN_MANAGER));
		add(new User("X", "x@email.com", "xxx", Profile.USER));
	}
	
	protected MemoryUserDAO() {
	}
	
	public User find(Integer userId) {
		return users.stream().filter(u -> u.getId().equals(userId)).findFirst().orElse(null);
	}
	
	public User find(String email) {
		return users.stream().filter(u -> u.getEmail().equals(email)).findFirst().orElse(null);
	}
	
	public User save(User user) {
		if(user.getNewPassword()!=null) {
			user.setPassword(user.getNewPassword());
			user.setNewPassword(null);
		}		
		return add(user);
	}
	
	private static User add(User newValue) {
		newValue.setId(++counter);
		boolean wasAdded = users.add(newValue);		
		return wasAdded? newValue:null;
	}
	
	public User update(User user) {
		User userToUpdate = find(user.getId());
		if(userToUpdate==null) return null;
		userToUpdate.setName(user.getName());
		userToUpdate.setEmail(user.getEmail());
		if(user.getPassword() != null && !user.getPassword().trim().isEmpty()) {
			userToUpdate.setPassword(user.getPassword());
		}
		userToUpdate.setProfile(user.getProfile());
		return userToUpdate;
	}
	
	public boolean remove(Integer userId) {
		User userToRemove = find(userId);
		return users.remove(userToRemove);
	}
	
	@Override
	public Set<User> findAll() {
		return users;
	}

}
