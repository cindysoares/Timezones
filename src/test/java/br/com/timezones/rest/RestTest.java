package br.com.timezones.rest;

import javax.ws.rs.client.WebTarget;

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
    	target = target(getBasePath()).register(authFilter);
	}

    @Override
    protected ResourceConfig configure() {
        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);

        return new JerseyConfig();
    }
    
}
