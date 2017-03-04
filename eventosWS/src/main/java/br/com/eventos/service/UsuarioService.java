package br.com.eventos.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import br.com.eventos.dao.UsuarioDAO;
import br.com.eventos.model.Usuario;
import br.com.eventos.model.dto.UsuarioDTO;

@Component
public class UsuarioService {

	@Autowired
	private UsuarioDAO dao;

	public UsuarioDTO logar(String email, String senha) {

		Usuario usuario = dao.logar(email, senha);
		return UsuarioDTO.createUsuarioDTO(usuario);
	}

	public UsuarioDTO save(Usuario usuario) {

		usuario = dao.save(usuario);
		return UsuarioDTO.createUsuarioDTO(usuario);
	}

	public UsuarioDTO update(Usuario usuario) {

		usuario = dao.update(usuario);
		return UsuarioDTO.createUsuarioDTO(usuario);
	}
}
