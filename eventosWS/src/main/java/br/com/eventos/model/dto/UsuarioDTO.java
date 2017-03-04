package br.com.eventos.model.dto;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;

import br.com.eventos.model.Usuario;

public class UsuarioDTO {

	private Long id;
	private String nome;
	private String email;
	private String senha;
	private boolean queroSerAdmin;
	private boolean administrador;
	private FaculdadeDTO faculdade;
	private String fotoPerfil;

	public static UsuarioDTO createUsuarioDTO(Usuario usuario) {

		ModelMapper mapper = new ModelMapper();
		java.lang.reflect.Type targetType = new TypeToken<UsuarioDTO>() {
		}.getType();
		return mapper.map(usuario, targetType);
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public boolean isQueroSerAdmin() {
		return queroSerAdmin;
	}

	public void setQueroSerAdmin(boolean queroSerAdmin) {
		this.queroSerAdmin = queroSerAdmin;
	}

	public boolean isAdministrador() {
		return administrador;
	}

	public void setAdministrador(boolean administrador) {
		this.administrador = administrador;
	}

	public FaculdadeDTO getFaculdade() {
		return faculdade;
	}

	public void setFaculdade(FaculdadeDTO faculdade) {
		this.faculdade = faculdade;
	}

	public String getFotoPerfil() {
		return fotoPerfil;
	}

	public void setFotoPerfil(String fotoPerfil) {
		this.fotoPerfil = fotoPerfil;
	}

}
