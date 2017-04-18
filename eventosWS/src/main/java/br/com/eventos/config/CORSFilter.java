package br.com.eventos.config;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

public class CORSFilter implements ContainerResponseFilter {
	@Override
	public void filter(ContainerRequestContext request, ContainerResponseContext response) {
		// Adiciona permições para acessar a API fora da mesma origin, autoriZA
		// outras APP a acessar meu WebService
		response.getHeaders().add("Access-Control-Allow-Origin", "*");
		response.getHeaders().add("Access-Control-Allow-Headers", "origin, content-type, accept, authorization");
		response.getHeaders().add("Access-Control-Allow-Credentials", "true");
		response.getHeaders().add("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
		response.getHeaders().add("Access-Control-Max-Age", "1209600");

		/*MediaType type = response.getMediaType();
		if (type != null) {
			String contentType = type.toString();
			if (!contentType.contains("charset")) {
				contentType = contentType + ";charset=utf-8";
				response.getHeaders().putSingle("Content-Type", contentType);
			}
		}*/
	}
}
