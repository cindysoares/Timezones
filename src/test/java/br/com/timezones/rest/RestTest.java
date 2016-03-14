package br.com.timezones.rest;

import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

public abstract class RestTest extends JerseyTest {

	protected HttpAuthenticationFeature authFilter;
	protected WebTarget target;

	protected abstract String getBasePath();

	@Override
	public void setUp() throws Exception {
		super.setUp();
		authFilter = HttpAuthenticationFeature.basicBuilder().build();
		target = target(getBasePath());
	}

	@Override
	protected ResourceConfig configure() {
		enable(TestProperties.LOG_TRAFFIC);
		enable(TestProperties.DUMP_ENTITY);

		return new JerseyConfig();
	}

	protected Builder requestBuilder() {
		return requestBuilder("/", "cindy@email.com", "senha");
	}

	protected Builder requestBuilder(String path) {
		return requestBuilder(path, "cindy@email.com", "senha");
	}
	
	protected Builder requestBuilder(String path, String email, String password) {
		return target.register(authFilter).path(path).request(MediaType.APPLICATION_JSON)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_USERNAME, email)
				.property(HttpAuthenticationFeature.HTTP_AUTHENTICATION_BASIC_PASSWORD, password);
	}

}
