package br.com.eventos.model.dto;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import br.com.eventos.model.Faculdade;

public class FaculdadeDTO {

	private Long id;
	private String nome;
	
	public static List<FaculdadeDTO> createListFaculdadeDTO(List<Faculdade> faculdades) {

		ModelMapper mapper = new ModelMapper();
		java.lang.reflect.Type targetType = new TypeToken<List<FaculdadeDTO>>() {
		}.getType();
		return mapper.map(faculdades, targetType);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}
}
