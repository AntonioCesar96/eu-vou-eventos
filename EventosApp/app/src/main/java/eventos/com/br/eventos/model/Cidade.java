package eventos.com.br.eventos.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

@DatabaseTable(tableName = "cidade")
public class Cidade implements Serializable {
	private static final long serialVersionUID = 1L;

	@DatabaseField(id = true, generatedId = false)
	private Long id;

	@DatabaseField
	private String nome;

	@DatabaseField(foreign = true)
	private Estado estado;

	public Cidade(){

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

	public Estado getEstado() {
		return estado;
	}

	public void setEstado(Estado estado) {
		this.estado = estado;
	}
}
