package br.com.eventos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.dao.FaculdadeDAO;
import br.com.eventos.model.Faculdade;
import br.com.eventos.model.dto.FaculdadeDTO;

@Component
public class FaculdadeService {

	@Autowired
	private FaculdadeDAO dao;

	public List<FaculdadeDTO> getFaculdades() {
		
		List<Faculdade> faculdades = dao.getFaculdades();
		return FaculdadeDTO.createListFaculdadeDTO(faculdades);
	}
}
