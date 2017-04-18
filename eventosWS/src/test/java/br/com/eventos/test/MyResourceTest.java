package br.com.eventos.test;

import static org.junit.Assert.assertEquals;

import javax.ws.rs.core.Application;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.Test;

import br.com.eventos.rest.EventosResource;

public class MyResourceTest extends JerseyTest {

	@Override
	protected Application configure() {
		return new ResourceConfig(EventosResource.class);
	}

	/**
	 * Test to see that the message "Got it!" is sent in the response.
	 */
	@Test
	public void testGetIt() {
		final String responseMsg = target().path("evento/test").request().get(String.class);

		assertEquals("AAAAA", responseMsg);
	}
}
