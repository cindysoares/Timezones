package br.com.timezones.rest;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import br.com.timezones.authentication.Credentials;

public abstract class RestTest extends JerseyTest {

	protected static final String AUTHORIZATION_PROPERTY = "Authorization";

	protected static final String LOGIN_BASE_PATH = "/login";
	
	protected static final String REGULAR_USER_PASSWORD = "senha";
	protected static final String REGULAR_USER_EMAIL = "cindy@email.com";
	
	protected static final String ADMIN_USER_PASSWORD = "4321";
	protected static final String ADMIN_USER_EMAIL = "admin@email.com";
	
	protected static final String MANAGER_USER_PASSWORD = "1234";
	protected static final String MANAGER_USER_EMAIL = "manager@email.com";

	protected WebTarget target;

	protected abstract String getBasePath();

	@Override
	public void setUp() throws Exception {
		super.setUp();
		target = target(getBasePath());
	}

	@Override
	protected ResourceConfig configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		return new JerseyConfig();
	}

	protected Builder requestBuilder() {
		return requestBuilder("/", REGULAR_USER_EMAIL, REGULAR_USER_PASSWORD);
	}
	
	protected Builder requestBuilder(String email, String password) {
		return requestBuilder("/", email, password);
	}

	protected Builder requestBuilder(String path) {
		return requestBuilder(path, REGULAR_USER_EMAIL, REGULAR_USER_PASSWORD);
	}
	
	protected Builder requestBuilder(String path, String email, String password) {
		String token = getAuthorizationToken(email, password);
		return target.path(path).request(MediaType.APPLICATION_JSON).header(AUTHORIZATION_PROPERTY, token);
	}

	private String getAuthorizationToken(String email, String password) {
		return target(LOGIN_BASE_PATH).request(MediaType.APPLICATION_JSON)
        	    .post(Entity.entity(new Credentials(email, password), MediaType.APPLICATION_JSON), String.class);
	}

}
