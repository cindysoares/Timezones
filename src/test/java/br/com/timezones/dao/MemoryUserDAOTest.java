package br.com.timezones.dao;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import br.com.timezones.model.User;

public class MemoryUserDAOTest {
	
	private MemoryUserDAO dao = new MemoryUserDAO();
	
	@Test
	public void testFindByEmail() {
		User user = dao.find("cindy@email.com");
		Assert.assertNotNull("Didn't find the user.", user);
		Assert.assertEquals("Wrong user.", "Cindy Soares", user.getName());
	}

	@Test
	public void testFindByEmailWhenUserDoesntExists() {
		User user = dao.find("xxx@email.com");
		Assert.assertNull("The user was found.", user);
	}

	@Test
	public void testFindById() {
		User user = dao.find(1);
		Assert.assertNotNull("Didn't find the user.", user);
		Assert.assertEquals("Wrong user.", "Cindy Soares", user.getName());
	}
	
	@Test
	public void testFindAll() {
		Set<User> users = dao.findAll();
		Assert.assertNotNull("Didn't find all users.", users);
		Assert.assertFalse("The list shouldn't be empty", users.isEmpty());		
	}

}
