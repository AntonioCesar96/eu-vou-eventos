package br.com.eventos.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.model.dto.CidadeDTO;
import br.com.eventos.model.dto.EstadoDTO;
import br.com.eventos.service.EnderecoService;

@Path("/")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Component
public class EnderecoResource {

	@Autowired
	private EnderecoService service;

	@GET
	@Path("/estados")
	public List<EstadoDTO> getEstados() {
		return service.getEstados();
	}

	@GET
	@Path("/cidades/{id}")
	public List<CidadeDTO> getCidades(@PathParam("id") Long idEstado) {
		return service.getCidades(idEstado);
	}
}
