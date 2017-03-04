package eventos.com.br.eventos.model;

import java.io.Serializable;

public class Cidade implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private Estado estado;

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

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
