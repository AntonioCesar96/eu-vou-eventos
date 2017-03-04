package br.com.eventos.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.dao.EnderecoDAO;
import br.com.eventos.model.Cidade;
import br.com.eventos.model.Estado;
import br.com.eventos.model.dto.CidadeDTO;
import br.com.eventos.model.dto.EstadoDTO;

@Component
public class EnderecoService {

	@Autowired
	private EnderecoDAO dao;

	public List<EstadoDTO> getEstados() {
		List<Estado> estados = dao.getEstados();
		return EstadoDTO.createListEstadosDTO(estados);
	}

	public List<CidadeDTO> getCidades(Long idEstado) {
		List<Cidade> cidades = dao.getCidades(idEstado);
		return CidadeDTO.createListCidadesDTO(cidades);
	}
}
