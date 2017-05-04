package br.com.eventos.config;

import org.glassfish.jersey.server.ResourceConfig;

//@ApplicationPath("/rest/*")
public class GeralResourceConfig extends ResourceConfig {

	public GeralResourceConfig() {
		packages("br.com.eventos");
		
		register(CORSFilter.class);
	}
}
