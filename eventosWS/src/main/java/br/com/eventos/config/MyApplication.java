package br.com.eventos.config;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;

public class MyApplication extends Application {

	@Override
	public Set<Object> getSingletons() {
		Set<Object> singletons = new HashSet<>();
		
		return singletons;
	}

	@Override
	public Map<String, Object> getProperties() {
		Map<String, Object> properties = new HashMap<>();
		properties.put("jersey.config.server.provider.packages", "br.com.eventos");

		return properties;
	}
	
	@Override
	public Set<Class<?>> getClasses() {
		Set<Class<?>> s = new HashSet<>();

		s.add(CORSFilter.class);
		return s;
	}
}