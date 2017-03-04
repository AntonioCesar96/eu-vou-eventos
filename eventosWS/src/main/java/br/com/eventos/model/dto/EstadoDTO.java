package br.com.eventos.model.dto;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import br.com.eventos.model.Estado;

public class EstadoDTO {

	private Long id;
	private String nome;
	private String uf;

	public static List<EstadoDTO> createListEstadosDTO(List<Estado> estados) {

		ModelMapper mapper = new ModelMapper();
		java.lang.reflect.Type targetType = new TypeToken<List<EstadoDTO>>() {
		}.getType();
		return mapper.map(estados, targetType);
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

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}
}
