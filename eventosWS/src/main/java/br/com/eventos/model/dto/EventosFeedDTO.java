package br.com.eventos.model.dto;

import java.util.Calendar;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import br.com.eventos.model.Evento;

public class EventosFeedDTO {

	private Long id;
	private String nome;
	private String enderecoImagem;
	private Calendar dataHora;
	private LocalFeedDTO local;

	public static List<EventosFeedDTO> createListEventoFeedDTO(List<Evento> eventos) {

		ModelMapper mapper = new ModelMapper();
		java.lang.reflect.Type targetType = new TypeToken<List<EventosFeedDTO>>() {
		}.getType();
		return mapper.map(eventos, targetType);
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

	public String getEnderecoImagem() {
		return enderecoImagem;
	}

	public void setEnderecoImagem(String enderecoImagem) {
		this.enderecoImagem = enderecoImagem;
	}

	public Calendar getDataHora() {
		return dataHora;
	}

	public void setDataHora(Calendar dataHora) {
		this.dataHora = dataHora;
	}

	public LocalFeedDTO getLocal() {
		return local;
	}

	public void setLocal(LocalFeedDTO local) {
		this.local = local;
	}

}
