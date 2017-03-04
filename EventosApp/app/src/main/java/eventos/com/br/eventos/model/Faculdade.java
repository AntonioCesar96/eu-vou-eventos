package eventos.com.br.eventos.model;

import java.io.Serializable;

public class Faculdade implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long id;
	private String nome;
	private Cidade cidade;

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
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
