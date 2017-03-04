package br.com.eventos.model.dto;

import java.util.Calendar;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import br.com.eventos.model.Evento;

public class EventoFullDTO {

	private Long id;
	private String nome;
	private String descricao;
	private String enderecoImagem;
	private Calendar dataHora;
	private UsuarioDTO usuario;
	private FaculdadeDTO faculdade;
	private String nomeAtletica;
	private LocalFullDTO local;

	public static List<EventoFullDTO> createListEventoFullDTO(List<Evento> eventos) {

		ModelMapper mapper = new ModelMapper();
		java.lang.reflect.Type targetType = new TypeToken<List<EventoFullDTO>>() {
		}.getType();
		return mapper.map(eventos, targetType);
	}

	public static EventoFullDTO createEventoFullDTO(Evento evento) {

		ModelMapper mapper = new ModelMapper();
		java.lang.reflect.Type targetType = new TypeToken<EventoFullDTO>() {
		}.getType();
		return mapper.map(evento, targetType);
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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public UsuarioDTO getUsuario() {
		return usuario;
	}

	public void setUsuario(UsuarioDTO usuario) {
		this.usuario = usuario;
	}

	public FaculdadeDTO getFaculdade() {
		return faculdade;
	}

	public void setFaculdade(FaculdadeDTO faculdade) {
		this.faculdade = faculdade;
	}

	public String getNomeAtletica() {
		return nomeAtletica;
	}

	public void setNomeAtletica(String nomeAtletica) {
		this.nomeAtletica = nomeAtletica;
	}

	public LocalFullDTO getLocal() {
		return local;
	}

	public void setLocal(LocalFullDTO local) {
		this.local = local;
	}

}
