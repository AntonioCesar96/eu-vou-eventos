package br.com.eventos.rest;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.model.dto.FaculdadeDTO;
import br.com.eventos.service.FaculdadeService;

@Path("/faculdades")
@Consumes(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
@Component
public class FaculdadeResource {

	@Autowired
	private FaculdadeService service;

	@GET
	public List<FaculdadeDTO> get() {
		return service.getFaculdades();
	}
}