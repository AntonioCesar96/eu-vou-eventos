package br.com.eventos.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.model.Usuario;
import br.com.eventos.model.dto.UsuarioDTO;
import br.com.eventos.service.UsuarioService;

@Path("/usuario")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Component
public class UsuarioResource {

	@Autowired
	private UsuarioService service;

	@GET
	public UsuarioDTO logar(@QueryParam("email") String email, @QueryParam("senha") String senha) {
		return service.logar(email, senha);
	}

	@POST
	public UsuarioDTO save(Usuario usuario) {
		return service.save(usuario);
	}

	@PUT
	public UsuarioDTO update(Usuario usuario) {
		return service.update(usuario);
	}
}